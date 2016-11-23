//
//  GameVC.h
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/10.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface GameVC : UIViewController

@property (strong, nonatomic) IBOutlet UIWebView *webView;
@property (nonatomic,strong) NSString* gameURL;
@property (nonatomic,strong) NSString* myResult;
@property (nonatomic,strong) NSString* gameResults;

@end
