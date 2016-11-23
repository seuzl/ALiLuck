//
//  GameSettingVC.h
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/11.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Activity.h"

@interface GameSettingVC : UITableViewController

@property (nonatomic,strong) Activity* activity;
@property BOOL isUpdate;

@end
