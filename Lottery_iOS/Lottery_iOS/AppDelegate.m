//
//  AppDelegate.m
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/9.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import "AppDelegate.h"
#import "IQKeyboardManager.h"
#import "Config.h"
#import "LoginVC.h"
#import "Activity+Request.h"
#import "AFNetworkReachabilityManager.h"

@interface AppDelegate ()

@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    IQKeyboardManager* manager = [IQKeyboardManager sharedManager];
    manager.enable = YES;
    manager.shouldResignOnTouchOutside = YES;
    manager.enableAutoToolbar = YES;
    
    self.blueManager = [[CBCentralManager alloc] initWithDelegate:self queue:nil];
    
    //    初始化connectManager:
    self.connectManager = [[ConnectManager alloc]init];
    self.connectManager.delegate = self;
    
    [self  initializeLocationService];
    
    NSLog(@"存储的acc:%@",[Config getAccessToken]);
    
    LoginVC* loginVC = (LoginVC*)[Config getVCFromSb:@"loginVC"];
    UINavigationController* nav = (UINavigationController*)[Config getVCFromSb:@"nav0"];
    if ([Config getAccessToken]) {
        NSLog(@"access_token:%@",[Config getAccessToken]);
        self.window.rootViewController = nav;
    } else {
        self.window.rootViewController = loginVC;
    }
    
    AFNetworkReachabilityManager *afmanager = [AFNetworkReachabilityManager sharedManager];
    
    [afmanager setReachabilityStatusChangeBlock:^(AFNetworkReachabilityStatus status) {
        
        switch (status) {
            case AFNetworkReachabilityStatusUnknown:
                break;
                
            case AFNetworkReachabilityStatusNotReachable:
//                没网
                if (![Config checkBlue]) {
                    [Config showErrorHUDwithStatus:@"无网络，建议开启蓝牙"];
                } else {
                    [self.connectManager startAdvertiser];
                    [self.connectManager startBrowser];
                }
                break;
                
            case AFNetworkReachabilityStatusReachableViaWWAN:
                break;
            case AFNetworkReachabilityStatusReachableViaWiFi:
                break;
        }
    }];
    
    //开始监控
    [afmanager startMonitoring];
    
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{

}

- (void)applicationWillEnterForeground:(UIApplication *)application
{

}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    
}

- (void)applicationWillTerminate:(UIApplication *)application
{

}

-(void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation
{
    [Config setLatitude:newLocation.coordinate.latitude];
    [Config setLongitude:newLocation.coordinate.longitude];
}

- (void)initializeLocationService {
    
    _locationManager= [[CLLocationManager alloc]init];
    _locationManager.delegate=self;
    _locationManager.desiredAccuracy=kCLLocationAccuracyBest;
    _locationManager.distanceFilter=kCLDistanceFilterNone;
    [_locationManager requestAlwaysAuthorization];
    [_locationManager startUpdatingLocation];
    
}

-(void)centralManagerDidUpdateState:(CBCentralManager *)central
{
    switch (central.state) {
        case CBCentralManagerStatePoweredOn:
            [Config setBlue:YES];
            [self.connectManager startBrowser];
            [self.connectManager startAdvertiser];
            break;
            
        case CBCentralManagerStatePoweredOff:
            [Config setBlue:NO];
            break;
            
        default:
            break;
    }
}

#pragma ConnectManagerDelegate

-(void)helpSendOthersDataWithManager:(ConnectManager *)manager data:(NSDictionary *)data peerID:(MCPeerID *)askedPerrID
{
    
    NSLog(@"B在帮A发请求!%@",data);
    if (![data[@"requestFlag"] intValue]) {
//        login:
        
        NSLog(@"B在帮A登录!");
        
        NSString* url = [NSString stringWithFormat:@"%@/login.do",[Config getMainURL]];
        [[Config getSessionManager] POST:url parameters:@{@"user_id":data[@"user_id"],@"password":data[@"password"]} progress:^(NSProgress * _Nonnull uploadProgress) {
            
            NSLog(@"登录中...");
            
        } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
            
            if (responseObject[@"data"][@"access_token"]) {
                
                NSLog(@"B帮助A获得 access_token:%@",responseObject[@"data"][@"access_token"]);
                [self.connectManager responseToAskWithData:@{@"requestFlag":@0,@"success":@YES,@"access_token":responseObject[@"data"][@"access_token"]} toPeer:askedPerrID];
                
            } else {
                
                NSLog(@"B帮助A登录失败!:%@",responseObject);
                [self.connectManager responseToAskWithData:@{@"requestFlag":@0,@"success":@NO} toPeer:askedPerrID];
            }
            
        } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
            NSLog(@"B帮助A登录失败!:%@",error);
            [self.connectManager responseToAskWithData:@{@"requestFlag":@0,@"success":@NO} toPeer:askedPerrID];
        }];
        
    } else {
//        game:
        NSLog(@"B在帮A找game！");
        
//        1.下载html
        dispatch_group_t group = dispatch_group_create();
        BOOL __block gameSuccess = YES;
        dispatch_group_enter(group);
        NSString* user_id = [Config getUserID];
        NSString* access_token = [Config getAccessToken];
        NSString* activity_id = data[@"activity_id"];
        NSString* longitude = data[@"longitude"];
        NSString* latitude = data[@"latitude"];
        NSString* reverse_id = data[@"user_id"];
        NSString* reverse_token = data[@"access_token"];
        
        NSString* gameurl = [NSString stringWithFormat:@"%@?user_id=%@&access_token=%@&activity_id=%@&longitude=%@&latitude=%@&reverse_id=%@&reverse_token=%@",[Config getQingWuURL],user_id,access_token,activity_id,longitude,latitude,reverse_id,reverse_token];
        NSLog(@"gameurl:%@",gameurl);
        
        NSURL *url = [NSURL URLWithString:gameurl];
        NSData *urlData = [NSData dataWithContentsOfURL:url];
        if (!urlData) {
            gameSuccess=NO;
        }
        dispatch_group_leave(group);
        
//        2.向服务器请求我的获奖结果
        dispatch_group_enter(group);
        [Activity fecthMyResultWithReverseToken:reverse_token reverse_id:reverse_id params:@{@"user_id":reverse_id,@"activity_id":activity_id,@"longitude":longitude,@"latitude":latitude} successBlock:^(id returnValue) {
            _myresult = [Config dictionaryToJson:returnValue];
            NSLog(@"B帮A获取A的游戏结果join.do成功!%@",_myresult);
            if ([returnValue[@"code"] isEqualToString:@"500"]) {
                gameSuccess=NO;
            }
            dispatch_group_leave(group);
            
        } failureBlock:^(NSError *error) {
            
            NSLog(@"B帮A获取A的游戏结果join.do失败!%@",error);
            gameSuccess=NO;
            dispatch_group_leave(group);
            
        }];
        
//        3.向服务器获取游戏奖项设置
        dispatch_group_enter(group);
        [Activity fetchRewardsWithReverseToken:reverse_token reverse_id:reverse_id activity_id:activity_id successBlock:^(id returnValue) {
           
            _gameResults = [Config dictionaryToJson:returnValue];
            if ([returnValue[@"code"] isEqualToString:@"500"]) {
                gameSuccess=NO;
            }
            NSLog(@"B帮A获取游戏奖项设置成功!%@",_gameResults);
            dispatch_group_leave(group);
            
        } failureBlock:^(NSError *error) {
            
            NSLog(@"B帮A获取游戏奖项设置失败!%@",error);
            gameSuccess=NO;
            dispatch_group_leave(group);
        }];
        
        
        dispatch_group_notify(group, dispatch_get_main_queue(), ^{
            
            if (gameSuccess) {
                [self.connectManager responseToAskWithData:@{@"requestFlag":@1, @"success":@YES, @"gameData":urlData,@"myResult":_myresult,@"gameResults":_gameResults} toPeer:askedPerrID];
            } else {
                [self.connectManager responseToAskWithData:@{@"requestFlag":@1,@"success":@NO} toPeer:askedPerrID];
            }
            
        });

    }
}

-(void)getFeedBackFromWhoHelpMeWithData:(NSDictionary *)dic
{
    [[NSOperationQueue mainQueue] addOperationWithBlock:^{
        
        self.feedBackBlock(dic);
        
    }];
    
}

-(void)connectedDevicesChangedWithManager:(ConnectManager *)manager connectedDevices:(NSMutableArray<NSString *> *)devices
{
    NSLog(@"connectedDevicesChange:已连接设备:%@",devices);
}

@end
