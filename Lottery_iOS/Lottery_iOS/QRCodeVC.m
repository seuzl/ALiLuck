//
//  QRCodeVC.m
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/11.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import "QRCodeVC.h"
#import "LBXScanWrapper.h"

@interface QRCodeVC ()

@end

@implementation QRCodeVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    UIImage* qrcodeImg = [LBXScanWrapper createQRWithString:self.acid size:self.qrcodeView.bounds.size];
    [self.qrcodeView setImage:qrcodeImg];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}



@end
