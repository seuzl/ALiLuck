//
//  CellWithTwoTextFields.h
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/11.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface CellWithTwoTextFields : UITableViewCell

@property (strong, nonatomic) IBOutlet UITextField *textField0;
@property (strong, nonatomic) IBOutlet UITextField *textField1;
@property (strong, nonatomic) IBOutlet UIButton *minus;
@property (strong, nonatomic) IBOutlet UIButton *add;

@end
