//
//  Config.h
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/9.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <AFNetworking/AFNetworking.h>
#import "ConnectManager.h"
#import "AppDelegate.h"

typedef void(^SuccessBlock)(id returnValue);
typedef void(^FailureBlock)(NSError *error);

@interface Config : NSObject

+(NSString *)getMainURL;
+(NSString *)getQingWuURL;
+(UIView *)getTableViewFooter;
+(CGFloat)getSectionHeaderHeight;
+(UIColor *)getTintColor;
+(UIViewController*)getVCFromSb:(NSString*)storyboardID;
+(AFHTTPSessionManager*)getSessionManager;
+(NSDictionary*)getInfoPlistDict;

//nsuserdefaults相关
+(void)setLongitude:(double)longitude;
+(double)getLongitude;

+(void)setLatitude:(double)latitude;
+(double)getLatitude;

+(void)setUserID:(NSString*)user_id;
+(NSString*)getUserID;

+(void)setAccessToken:(NSString*)access_token;
+(NSString*)getAccessToken;

+(void)removeAllObject;

+(void)showProgressHUDwithStatus:(NSString*) status;
+(void)dismissHUD;
+(void)showSuccessHUDwithStatus:(NSString*) status;
+(void)showErrorHUDwithStatus:(NSString*) status;

+(int)compareDate:(NSString*)date01 withDate:(NSString*)date02;
+(NSString*)getCurrentDateString;

+(BOOL)checkConnect;
+(BOOL)checkBlue;
+(void)setBlue:(BOOL)blueOn;

+(NSString*)getGameFilePathWithMyResult:(NSString*)myResult gameResults:(NSString*)gameResults;
+(NSString*)getEncryptedPwd:(NSString*)password;

+(NSString*)getpath;
+(NSString *)removeSpaceAndNewline:(NSString *)str;
+(NSString*)dictionaryToJson:(NSDictionary *)dic;

+(ConnectManager*)getConnectManager;

@end
