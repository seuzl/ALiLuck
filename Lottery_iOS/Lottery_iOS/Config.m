//
//  Config.m
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/9.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import "Config.h"
#import "SVProgressHUD.h"
#import <CommonCrypto/CommonCrypto.h>
#import "RSA.h"

@implementation Config

static NSString* timeStamp = nil;
static NSString* noce = nil;

+(void)initialize
{
    [SVProgressHUD setMinimumDismissTimeInterval:2.0];
}

+(NSString *)getMainURL
{
//    return @"http://30.4.140.16:8080/home";
    return @"http://11.239.174.45:7001/home";
}

+(NSString *)getQingWuURL
{
    return @"http://11.239.174.45:7001";
//    return @"http://30.4.140.16:8080";
}

+(BOOL)checkConnect
{
    NSString* url = @"https://www.baidu.com";
    NSURL *url1 = [NSURL URLWithString:url];
    NSURLRequest *request = [NSURLRequest requestWithURL:url1 cachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData timeoutInterval:5];
    NSHTTPURLResponse *response;
    [NSURLConnection sendSynchronousRequest:request returningResponse: &response error: nil];
    if (response == nil) {
        return NO;
    }
    else{
        return YES;
    }
}

+(void)setBlue:(BOOL)blueOn
{
    NSUserDefaults* userDefault = [NSUserDefaults standardUserDefaults];
    [userDefault setObject:[NSNumber numberWithBool:blueOn] forKey:@"blueOn"];
    [userDefault synchronize];
}

+(BOOL)checkBlue
{
    return [[[NSUserDefaults standardUserDefaults] objectForKey:@"blueOn"] boolValue];
}

+(UIView *)getTableViewFooter
{
    UIView *view = [[UIView alloc]init];
    view.backgroundColor = [UIColor clearColor];
    
    return view;
}

+(CGFloat)getSectionHeaderHeight
{
    return 8.0f;
}

+(UIColor *)getTintColor
{
    return [UIColor colorWithRed:0.914 green:0.363 blue:0.145 alpha:1];
}

+(UIViewController*)getVCFromSb:(NSString*)storyboardID
{
    UIStoryboard* story = [UIStoryboard storyboardWithName:@"Main" bundle:[NSBundle mainBundle]];
    UIViewController* VCFromSb = [story instantiateViewControllerWithIdentifier:storyboardID];
    
    return VCFromSb;
}

+(AFHTTPSessionManager*)getSessionManager
{
    AFHTTPSessionManager* manager = [[AFHTTPSessionManager alloc]init];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[Config getAccessToken] forHTTPHeaderField:@"access_token"];
    [manager.requestSerializer setValue:[Config getUserID] forHTTPHeaderField:@"user_id"];
    [manager.requestSerializer setValue:nil forHTTPHeaderField:@"reverse_token"];
    [manager.requestSerializer setValue:nil forHTTPHeaderField:@"reverse_id"];

    return manager;
}


+(NSDictionary*)getInfoPlistDict{
    NSString *path = [[NSBundle mainBundle] pathForResource:@"Info" ofType:@"plist"];
    return [NSDictionary dictionaryWithContentsOfFile:path];
}

+(NSString*)getGameFilePathWithMyResult:(NSString*)myResult gameResults:(NSString*)gameResults
{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *filePath = [NSString stringWithFormat:@"%@/%@?myResult=%@&gameResults=%@&type=1&status=1.html", [paths objectAtIndex:0],@"game.html",myResult,gameResults];
    return [Config removeSpaceAndNewline:filePath];
}

+(NSString*)getpath
{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *filePath = [NSString stringWithFormat:@"%@/%@", [paths objectAtIndex:0],@"game.html"];
    return filePath;
}

+(void)showProgressHUDwithStatus:(NSString*) status
{
    [SVProgressHUD showWithStatus:status];
}

+(void)dismissHUD
{
    [SVProgressHUD dismiss];
}

+(void)showSuccessHUDwithStatus:(NSString*) status
{
    [SVProgressHUD showSuccessWithStatus:status];
}

+(void)showErrorHUDwithStatus:(NSString*) status
{
    [SVProgressHUD showErrorWithStatus:status];
}

+(void)setLongitude:(double)longitude
{
    NSUserDefaults* userDefault = [NSUserDefaults standardUserDefaults];
    [userDefault setObject:[NSNumber numberWithDouble:longitude] forKey:@"longitude"];
    [userDefault synchronize];
}
+(double)getLongitude
{
    NSUserDefaults* userDefault = [NSUserDefaults standardUserDefaults];
    return [[userDefault objectForKey:@"longitude"] doubleValue];
}
+(void)setLatitude:(double)latitude
{
    NSUserDefaults* userDefault = [NSUserDefaults standardUserDefaults];
    [userDefault setObject:[NSNumber numberWithDouble:latitude] forKey:@"latitude"];
    [userDefault synchronize];
}
+(double)getLatitude
{
    return [[[NSUserDefaults standardUserDefaults] objectForKey:@"latitude"] doubleValue];
}
+(void)setUserID:(NSString*)user_id
{
    NSUserDefaults* userDefault = [NSUserDefaults standardUserDefaults];
    [userDefault setObject:user_id forKey:@"user_id"];
    [userDefault synchronize];
}
+(NSString*)getUserID
{
    return [[NSUserDefaults standardUserDefaults] objectForKey:@"user_id"];
}
+(void)setAccessToken:(NSString*)access_token
{
    NSUserDefaults* userDefault = [NSUserDefaults standardUserDefaults];
    [userDefault setObject:access_token forKey:@"access_token"];
    [userDefault synchronize];
}
+(NSString*)getAccessToken
{
    return [[NSUserDefaults standardUserDefaults] objectForKey:@"access_token"];
}
+(void)removeAllObject
{
    NSUserDefaults *defatluts = [NSUserDefaults standardUserDefaults];
    NSDictionary *dictionary = [defatluts dictionaryRepresentation];
    for(NSString *key in [dictionary allKeys]){
        if (![key isEqualToString:@"blueOn"]) {
            [defatluts removeObjectForKey:key];
            [defatluts synchronize];
        }
    }
}
+(int)compareDate:(NSString*)date01 withDate:(NSString*)date02{
    int ci;
    NSDateFormatter *df = [[NSDateFormatter alloc] init];
    [df setDateFormat:@"yyyy-MM-dd HH:mm"];
    NSDate *dt1 = [[NSDate alloc] init];
    NSDate *dt2 = [[NSDate alloc] init];
    dt1 = [df dateFromString:date01];
    dt2 = [df dateFromString:date02];
    NSComparisonResult result = [dt1 compare:dt2];
    switch (result)
    {
            //date02比date01大
        case NSOrderedAscending: ci=-1; break;
            //date02比date01小
        case NSOrderedDescending: ci=1; break;
            //date02=date01
        case NSOrderedSame: ci=0; break;
        default: NSLog(@"erorr dates %@, %@", dt2, dt1); break;
    }
    return ci;
}

+(NSString*)getCurrentDateString
{
    NSDate *currentDate = [NSDate date];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm"];
    NSString *currentDateString = [dateFormatter stringFromDate:currentDate];
    
    return currentDateString;
}

+(NSString*)getEncryptedPwd:(NSString*)password
{
    NSString* encrypted = [RSA encryptString:password publicKey:@"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5KzvrTSasuQC2n4JAccBrhXDhLsdx20+7cKymDBmrG+sdWWyeGPmXdZfInA8hdlQ/pmyE5BKYYbCgKgm87HhAS2lvUXsfCpWXtgpLrWYDDYpHUHSyolG6yGRZrRkeAhNaYlMg9pNRMEfRhplDkc9RW29AdNNzAWxqDrAB+SMk9wIDAQAB"];
    return encrypted;
}

+(NSString *)removeSpaceAndNewline:(NSString *)str
{
    NSString *temp = [str stringByReplacingOccurrencesOfString:@" " withString:@""];
    temp = [temp stringByReplacingOccurrencesOfString:@"\r" withString:@""];
    temp = [temp stringByReplacingOccurrencesOfString:@"\n" withString:@""];
    return temp;
}

+ (NSString*)dictionaryToJson:(NSDictionary *)dic
{
    NSError *parseError = nil;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dic options:NSJSONWritingPrettyPrinted error:&parseError];
    return [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
}

+(ConnectManager*)getConnectManager
{
    AppDelegate* delegate = (AppDelegate*)[UIApplication sharedApplication].delegate;
    return delegate.connectManager;
}

@end

