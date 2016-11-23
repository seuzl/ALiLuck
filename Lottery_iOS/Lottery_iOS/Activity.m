//
//  Activity.m
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/11.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import "Activity.h"

@implementation Activity

+(NSDictionary*)mj_objectClassInArray
{
    return @{@"prize":@"PriceCategory",
             @"participants":@"Person"};
}

@end
