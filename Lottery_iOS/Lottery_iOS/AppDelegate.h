//
//  AppDelegate.h
//  Lottery_iOS
//
//  Created by 赵磊 on 26/8/9.
//  Copyright © 2526年 赵磊. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import <CoreBluetooth/CoreBluetooth.h>
#import "ConnectManager.h"

typedef void (^FeedBackBlock)(NSDictionary*dic);

@interface AppDelegate : UIResponder <UIApplicationDelegate,CLLocationManagerDelegate,CBCentralManagerDelegate,ConnectManagerDelegate>

@property (strong, nonatomic) UIWindow *window;
@property (strong,nonatomic) CLLocationManager*locationManager;
@property (nonatomic, strong) CBCentralManager *blueManager;
@property (strong, nonatomic) ConnectManager* connectManager;

@property FeedBackBlock feedBackBlock;

@property (nonatomic,strong) NSString* myresult;
@property (nonatomic,strong) NSString* gameResults;

@end


