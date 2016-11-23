//
//  HomeCell.h
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/10.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HomeCell : UITableViewCell


@property (strong, nonatomic) IBOutlet UIImageView *icon;

@property (strong, nonatomic) IBOutlet UILabel *statusLabel;

@property (strong, nonatomic) IBOutlet UILabel *titleLabel;

@property (strong, nonatomic) IBOutlet UILabel *organizerLabel;

@property (strong, nonatomic) IBOutlet UILabel *organizedTimeLabel;

@end
