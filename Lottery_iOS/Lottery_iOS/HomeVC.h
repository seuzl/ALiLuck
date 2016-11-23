//
//  HomeVC.h
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/10.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Config.h"
#import "Activity+Request.h"

@interface HomeVC : UIViewController

@property(nonatomic,strong) NSMutableArray<Activity*>* askArray;
@property(nonatomic,strong) NSMutableArray<Activity*>* sendArray;

@end
