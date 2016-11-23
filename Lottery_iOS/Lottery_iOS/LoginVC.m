//
//  LoginVC.m
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/9.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import "LoginVC.h"
#import "Config.h"
#import "AppDelegate.h"

@interface LoginVC ()

@end

@implementation LoginVC

- (void)viewDidLoad {
    [super viewDidLoad];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (IBAction)login:(UIButton *)sender
{
    
    if (![self.accountTextField.text isEqualToString:@""] && ![self.pwdTextField.text isEqualToString:@""]) {
        
         [Config showProgressHUDwithStatus:@"登录中"];
        
        if (![Config checkConnect]) {
            
            if ([Config checkBlue]) {
                
                ConnectManager* connectManager = [Config getConnectManager];
                if (connectManager.session.connectedPeers.count) {
                    
                    NSLog(@"无网：转发中");
                    AppDelegate* appDelegate = (AppDelegate*)[UIApplication sharedApplication].delegate;
                    NSString* encryped = [Config getEncryptedPwd:self.pwdTextField.text];
                    [appDelegate.connectManager askOthersHelpSendData:@{@"requestFlag":@0,@"user_id":self.accountTextField.text,@"password":encryped}];
                    
                    appDelegate.feedBackBlock = ^(NSDictionary*dic){
                        if ([dic[@"success"] boolValue]) {
                            [Config setAccessToken:[NSString stringWithFormat:@"%@",dic[@"access_token"]]];
                            [Config setUserID:self.accountTextField.text];
                            [Config dismissHUD];
                            [self performSegueWithIdentifier:@"toHomeNav" sender:nil];
                            
                        } else {
                            [Config showErrorHUDwithStatus:@"登录失败!"];
                        }
                    };
                    
                } else {
                    [Config showErrorHUDwithStatus:@"当前无人与您建联，请稍后再试~"];
                    [[Config getConnectManager] startBrowser];
                    [[Config getConnectManager] startAdvertiser];
                }
            } else {
                [Config showErrorHUDwithStatus:@"建议您开启蓝牙，再试试~"];
            }
            
        } else {
            
            NSString* url = [NSString stringWithFormat:@"%@/login.do",[Config getMainURL]];
           
            [[Config getSessionManager] POST:url parameters:@{@"user_id":self.accountTextField.text,@"password":[Config getEncryptedPwd:self.pwdTextField.text]} progress:^(NSProgress * _Nonnull uploadProgress) {
                
                
            } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
                
                [Config setUserID:self.accountTextField.text];
                
                if (responseObject[@"data"][@"access_token"]) {
                    NSLog(@"access_token:%@",responseObject[@"data"][@"access_token"]);
                    [Config setAccessToken:responseObject[@"data"][@"access_token"]];
                    [Config dismissHUD];
                    [self performSegueWithIdentifier:@"toHomeNav" sender:nil];
                } else {
                    [Config showErrorHUDwithStatus:@"登录失败"];
                }
                
                
            } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
                
                [Config showErrorHUDwithStatus:@"登录失败!"];
            }];
            
        }
        
    } else {
        [Config showErrorHUDwithStatus:@"信息不完整!"];
    }
}


@end
