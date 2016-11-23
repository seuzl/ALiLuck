//
//  TBPatchEncryption.m
//  APatch
//
//  Created by lianyu.ysj on 16/5/25.
//  Copyright © 2016年 alibaba. All rights reserved.
//

#import "TBPatchEncryption.h"
#import <CommonCrypto/CommonCryptor.h>
#include <CommonCrypto/CommonDigest.h>
#include <CommonCrypto/CommonHMAC.h>

/**
 * Base64 解码。
 */
static NSData * base64Decode(NSString * str){
	if (!str) {
		return nil;
	}
	
	NSData * data = [[NSData alloc] initWithBase64EncodedString:str options:NSDataBase64DecodingIgnoreUnknownCharacters];
	if (data.length > 0) {
		return data;
	}
	return nil;
}

@implementation TBPatchEncryption

#pragma mark- HmacSHA1

/**
 * HmacSHA1 签名。
 */
+ (NSString *)hmacSha1:(NSString *)data withKey:(NSString *)key {
	if (!data || !key) {
		return nil;
	}
	
	const char * cKey  = [key cStringUsingEncoding:NSASCIIStringEncoding];
	const char * cData = [data cStringUsingEncoding:NSASCIIStringEncoding];
	
	// SHA1
	unsigned char cHMAC[CC_SHA1_DIGEST_LENGTH];
	CCHmac(kCCHmacAlgSHA1, cKey, strlen(cKey), cData, strlen(cData), cHMAC);
	
	NSMutableString * result = [NSMutableString stringWithCapacity:CC_SHA1_DIGEST_LENGTH * 2];
	
	for(int i = 0; i < CC_SHA1_DIGEST_LENGTH; i++) {
		[result appendFormat:@"%02x", cHMAC[i]];
	}
	
	return result;
}

/**
 * 带有日期的 HmacSHA1 签名。
 */
+ (NSString *)hmacSha1WithDate:(NSString *)data withKey:(NSString *)key {
	if (!data || !key) {
		return nil;
	}
	
	static NSDateFormatter * formatter;
	static dispatch_once_t onceToken;
	dispatch_once(&onceToken, ^{
		formatter = [[NSDateFormatter alloc] init];
		[formatter setLocale:[NSLocale localeWithLocaleIdentifier:@"en_US_POSIX"]];
		[formatter setTimeZone:[NSTimeZone timeZoneWithName:@"GMT"]];
		[formatter setDateFormat:@"yyyyMMdd"];
	});
	
	NSString * dateString = [formatter stringFromDate:[NSDate date]];
	return [self hmacSha1:[data stringByAppendingString:dateString] withKey:key];
}

#pragma mark- RSA

/**
 * 使用 RSA 私钥解密。
 */
+ (NSString *)rsaDecryptString:(NSString *)str withPrivateKey:(NSString *)privateKey {
	if (!str) {
		return nil;
	}
	
	// 要解密的数据。
	NSData * data = base64Decode(str);
	if (!data) {
		return nil;
	}
	
	// 获取私钥。
	SecKeyRef keyRef = [self getPrivateKeyWithBase64:privateKey];
	if (!keyRef) {
		return nil;
	}
	
	// RSA 解密。
	data = [self rsaDecryptData:data withKeyRef:keyRef];
	return [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
}

/**
 * 从私钥的 Base64 得到私钥。
 */
+ (SecKeyRef)getPrivateKeyWithBase64:(NSString *)key {
	NSRange spos = [key rangeOfString:@"-----BEGIN RSA PRIVATE KEY-----"];
	NSRange epos = [key rangeOfString:@"-----END RSA PRIVATE KEY-----"];
	if(spos.location != NSNotFound && epos.location != NSNotFound){
		NSUInteger s = spos.location + spos.length;
		NSUInteger e = epos.location;
		NSRange range = NSMakeRange(s, e-s);
		key = [key substringWithRange:range];
	}
	key = [key stringByReplacingOccurrencesOfString:@"\r" withString:@""];
	key = [key stringByReplacingOccurrencesOfString:@"\n" withString:@""];
	key = [key stringByReplacingOccurrencesOfString:@"\t" withString:@""];
	key = [key stringByReplacingOccurrencesOfString:@" "  withString:@""];
	
	// This will be base64 encoded, decode it.
	NSData * data = base64Decode(key);
	data = [self stripPrivateKeyHeader:data];
	if(!data){
		return nil;
	}
	
	//a tag to read/write keychain storage
	NSString *tag = @"RSAUtil_PrivKey";
	NSData *d_tag = [NSData dataWithBytes:[tag UTF8String] length:[tag length]];
	
	// Delete any old lingering key with the same tag
	NSMutableDictionary *privateKey = [[NSMutableDictionary alloc] init];
	[privateKey setObject:(__bridge id) kSecClassKey forKey:(__bridge id)kSecClass];
	[privateKey setObject:(__bridge id) kSecAttrKeyTypeRSA forKey:(__bridge id)kSecAttrKeyType];
	[privateKey setObject:d_tag forKey:(__bridge id)kSecAttrApplicationTag];
	SecItemDelete((__bridge CFDictionaryRef)privateKey);
	
	// Add persistent version of the key to system keychain
	[privateKey setObject:data forKey:(__bridge id)kSecValueData];
	[privateKey setObject:(__bridge id) kSecAttrKeyClassPrivate forKey:(__bridge id)
	 kSecAttrKeyClass];
	[privateKey setObject:[NSNumber numberWithBool:YES] forKey:(__bridge id)
	 kSecReturnPersistentRef];
	
	CFTypeRef persistKey = nil;
	OSStatus status = SecItemAdd((__bridge CFDictionaryRef)privateKey, &persistKey);
	if (persistKey != nil){
		CFRelease(persistKey);
	}
	if ((status != noErr) && (status != errSecDuplicateItem)) {
		return nil;
	}
	
	[privateKey removeObjectForKey:(__bridge id)kSecValueData];
	[privateKey removeObjectForKey:(__bridge id)kSecReturnPersistentRef];
	[privateKey setObject:[NSNumber numberWithBool:YES] forKey:(__bridge id)kSecReturnRef];
	[privateKey setObject:(__bridge id) kSecAttrKeyTypeRSA forKey:(__bridge id)kSecAttrKeyType];
	
	// Now fetch the SecKeyRef version of the key
	SecKeyRef keyRef = nil;
	status = SecItemCopyMatching((__bridge CFDictionaryRef)privateKey, (CFTypeRef *)&keyRef);
	if(status != noErr){
		return nil;
	}
	return keyRef;
}

+ (NSData *)stripPrivateKeyHeader:(NSData *)d_key {
	// Skip ASN.1 private key header
	if (d_key == nil) return(nil);
	
	unsigned long len = [d_key length];
	if (!len) return(nil);
	
	unsigned char *c_key = (unsigned char *)[d_key bytes];
	unsigned int  idx     = 22; //magic byte at offset 22
	
	if (0x04 != c_key[idx++]) return nil;
	
	//calculate length of the key
	unsigned int c_len = c_key[idx++];
	int det = c_len & 0x80;
	if (!det) {
		c_len = c_len & 0x7f;
	} else {
		int byteCount = c_len & 0x7f;
		if (byteCount + idx > len) {
			//rsa length field longer than buffer
			return nil;
		}
		unsigned int accum = 0;
		unsigned char *ptr = &c_key[idx];
		idx += byteCount;
		while (byteCount) {
			accum = (accum << 8) + *ptr;
			ptr++;
			byteCount--;
		}
		c_len = accum;
	}
	
	// Now make a new NSData from this buffer
	return [d_key subdataWithRange:NSMakeRange(idx, c_len)];
}

/**
 * RSA 解密。
 */
+ (NSData *)rsaDecryptData:(NSData *)data withKeyRef:(SecKeyRef)keyRef {
	const uint8_t *srcbuf = (const uint8_t *)[data bytes];
	size_t srclen = (size_t)data.length;
	
	size_t block_size = SecKeyGetBlockSize(keyRef) * sizeof(uint8_t);
	UInt8 *outbuf = malloc(block_size);
	size_t src_block_size = block_size;
	
	NSMutableData *ret = [[NSMutableData alloc] init];
	for(int idx=0; idx<srclen; idx+=src_block_size){
		//NSLog(@"%d/%d block_size: %d", idx, (int)srclen, (int)block_size);
		size_t data_len = srclen - idx;
		if(data_len > src_block_size){
			data_len = src_block_size;
		}
		
		size_t outlen = block_size;
		OSStatus status = noErr;
		status = SecKeyDecrypt(keyRef,
							   kSecPaddingNone,
							   srcbuf + idx,
							   data_len,
							   outbuf,
							   &outlen
							   );
		if (status != 0) {
			NSLog(@"SecKeyEncrypt fail. Error Code: %d", (int)status);
			ret = nil;
			break;
		}else{
			//the actual decrypted data is in the middle, locate it!
			int idxFirstZero = -1;
			int idxNextZero = (int)outlen;
			for ( int i = 0; i < outlen; i++ ) {
				if ( outbuf[i] == 0 ) {
					if ( idxFirstZero < 0 ) {
						idxFirstZero = i;
					} else {
						idxNextZero = i;
						break;
					}
				}
			}
			
			[ret appendBytes:&outbuf[idxFirstZero+1] length:idxNextZero-idxFirstZero-1];
		}
	}
	
	free(outbuf);
	CFRelease(keyRef);
	return ret;
}

#pragma mark- AES256

/**
 * 使用 AES256 解密。
 */
+ (NSData *)aes256Decrypt:(NSData *)data withKey:(NSString *)key iv:(NSString *)iv {
	// 'key' should be 32 bytes for AES256, will be null-padded otherwise
	char keyPtr[kCCKeySizeAES256+1]; // room for terminator (unused)
	bzero(keyPtr, sizeof(keyPtr)); // fill with zeroes (for padding)
	
	// fetch key data
	[key getCString:keyPtr maxLength:sizeof(keyPtr) encoding:NSUTF8StringEncoding];
	
	char ivPtr[kCCKeySizeAES256+1];
	bzero(ivPtr, sizeof(ivPtr));
	[iv getCString:ivPtr maxLength:sizeof(ivPtr) encoding:NSUTF8StringEncoding];
	
	NSUInteger dataLength = data.length;
	
	//See the doc: For block ciphers, the output size will always be less than or
	//equal to the input size plus the size of one block.
	//That's why we need to add the size of one block here
	size_t bufferSize = dataLength + kCCBlockSizeAES128;
	void *buffer = malloc(bufferSize);
	
	size_t numBytesDecrypted = 0;
	CCCryptorStatus cryptStatus = CCCrypt(kCCDecrypt,
										  kCCAlgorithmAES128,
										  kCCOptionPKCS7Padding,
										  keyPtr,
										  kCCKeySizeAES128,
										  ivPtr /* initialization vector (optional) */,
										  [data bytes],
										  dataLength, /* input */
										  buffer,
										  bufferSize, /* output */
										  &numBytesDecrypted);
	
	if (cryptStatus == kCCSuccess) {
		//the returned NSData takes ownership of the buffer and will free it on deallocation
		return [NSData dataWithBytesNoCopy:buffer length:numBytesDecrypted];
	}
	
	free(buffer); //free the buffer;
	return nil;
}

@end
