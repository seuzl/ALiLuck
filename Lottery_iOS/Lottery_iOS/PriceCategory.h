//
//  PriceCategory.h
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/11.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "MJExtension.h"
#import "Person.h"

@interface PriceCategory : NSObject

@property NSString* name;
@property NSInteger number;
@property NSArray<Person*>* lucky;

@end
