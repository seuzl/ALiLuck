//
//  QRCodeVC.h
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/11.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface QRCodeVC : UIViewController

@property (strong, nonatomic) IBOutlet UIImageView *qrcodeView;
@property (strong, nonatomic) NSString* acid;

@end
