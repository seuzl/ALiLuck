//
//  InviteVC.h
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/11.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Person.h"

typedef void (^PersonsBlock)(NSMutableArray<Person*> *candidates);

@interface InviteVC : UITableViewController

@property (strong, nonatomic) PersonsBlock personsBlock;

@end
