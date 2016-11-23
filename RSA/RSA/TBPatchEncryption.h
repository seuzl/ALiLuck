//
//  TBPatchEncryption.h
//  APatch
//
//  Created by lianyu.ysj on 16/5/25.
//  Copyright © 2016年 alibaba. All rights reserved.
//

#import <Foundation/Foundation.h>

/**
 * TBPatch 使用的加解密算法。
 */
@interface TBPatchEncryption : NSObject 

/**
 * HmacSHA1 签名。
 */
+ (NSString *)hmacSha1:(NSString *)data withKey:(NSString *)key;

/**
 * 带有日期的 HmacSHA1 签名。
 */
+ (NSString *)hmacSha1WithDate:(NSString *)data withKey:(NSString *)key;

/**
 * 使用 RSA 私钥解密。
 */
+ (NSString *)rsaDecryptString:(NSString *)str withPrivateKey:(NSString *)privateKey;

/**
 * 使用 AES256 解密。
 */
+ (NSData *)aes256Decrypt:(NSData *)data withKey:(NSString *)key iv:(NSString *)iv;

@end
