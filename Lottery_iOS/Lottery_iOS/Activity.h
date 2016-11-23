//
//  Activity.h
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/11.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "PriceCategory.h"
#import "MJExtension.h"
#import "Config.h"
#import "Person.h"

@interface Activity : NSObject

@property NSString* creator_id;
@property NSString* creator_name;
@property NSInteger activity_id;
@property NSString* location;
@property NSString* title;
@property NSString* start_time;
@property NSString* create_time;
@property NSString* end_time;
@property NSMutableArray<Person*>* participants;
@property double longitude;
@property double latitude;
@property NSInteger limit;
@property NSMutableArray<PriceCategory*>* prize;

@end
