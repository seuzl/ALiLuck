//
//  GameVC.m
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/10.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import "GameVC.h"
#import "Config.h"

@interface GameVC ()

@end

@implementation GameVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    NSLog(@"gameurl:%@",_gameURL);
    
    if (_gameURL) {
        
        [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:self.gameURL]]];
    
    } else {
        
        [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL fileURLWithPath:[Config getGameFilePathWithMyResult:_myResult gameResults:_gameResults]]]];
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}



@end
