//
//  main.m
//  RSA
//
//  Created by 赵磊 on 16/8/16.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RSA.h"

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        
        NSString *pubkey = @"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5KzvrTSasuQC2n4JAccBrhXDhLsdx20+7cKymDBmrG+sdWWyeGPmXdZfInA8hdlQ/pmyE5BKYYbCgKgm87HhAS2lvUXsfCpWXtgpLrWYDDYpHUHSyolG6yGRZrRkeAhNaYlMg9pNRMEfRhplDkc9RW29AdNNzAWxqDrAB+SMk9wIDAQAB";
        NSString* encrypted = [RSA encryptData:<#(NSData *)#> publicKey:<#(NSString *)#>]
            
        
        
    }
    return 0;
}
