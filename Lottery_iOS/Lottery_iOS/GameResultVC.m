//
//  GameResultVC.m
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/11.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import "GameResultVC.h"
#import "Config.h"
#import "GameStatusCell.h"
#import "GameResultCell.h"
#import "Person.h"
#import "MJRefresh.h"
#import "QRCodeVC.h"
#import "Activity+Request.h"
#import "GameSettingVC.h"

@interface GameResultVC ()
{
    Activity* ac;
    int status;
}

@end

@implementation GameResultVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.tableView.tableFooterView = [Config getTableViewFooter];
    self.tableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefreshing)];
    ac = [[Activity alloc]init];
    
    [self fetchResult];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

-(void)fetchResult
{
    [Activity fetchActivityInfoWithReverseToken:nil reverse_id:nil activity_id:self.acid successBlock:^(id returnValue) {
        
        ac = [Activity mj_objectWithKeyValues:returnValue];
        
        NSString* start_time = ac.start_time;
        NSString* end_time = ac.end_time;
        
        int start_com =[Config compareDate:[Config getCurrentDateString] withDate:start_time];
        int end_com = [Config compareDate:[Config getCurrentDateString] withDate:end_time];
        
        if (start_com>=0) {
            if (end_com<=0) {
                status = 2;//进行中
            } else {
                status =3 ;//已结束
            }
        } else {
            status = 1;//活动未开始
            [self.tableView.mj_header endRefreshing];
        }
        
        [self.tableView reloadData];
        
        if (status!=1) {
            
            [Activity fetchActivityResultWithReverseToken:nil reverse_id:nil activity_id:self.acid successBlock:^(id returnValue) {
                
                ac.prize = [PriceCategory mj_objectArrayWithKeyValuesArray:returnValue];
                [self.tableView.mj_header endRefreshing];
                [self.tableView reloadData];
                
            } failureBlock:^(NSError *error) {
                
                [self.tableView.mj_header endRefreshing];
                NSLog(@"获取活动结果失败!%@",error);
                
            }];
            
        }
        
    } failureBlock:^(NSError *error) {
        
        [self.tableView.mj_header endRefreshing];
        NSLog(@"获取活动详情失败！%@",error);
    }];
    

}

-(void)headerRefreshing
{
    [self fetchResult];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    if (status!=1) {
        return 1+ac.prize.count;
    } else {
        return 2;
    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (!section) {
        return 1;
    } else if(section == 1){
        if (status==1) {
            return 1;
        } else {
            PriceCategory* price = ac.prize[section-1];
            return 1+price.lucky.count;
        }
    } else{
        PriceCategory* price = ac.prize[section-1];
        return 1+price.lucky.count;
    }
    
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (!indexPath.section) {
        return 80.0;
    } else {
        return 44.0;
    }
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return [Config getSectionHeaderHeight];
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    if (status==1 && indexPath.section==1) {
        
        [self performSegueWithIdentifier:@"toEdit" sender:nil];
    }
}

-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSInteger section = indexPath.section;
    NSInteger row = indexPath.row;
    
    if (!section) {
        GameStatusCell* cell = (GameStatusCell*)[tableView dequeueReusableCellWithIdentifier:@"GameStatusCell"];
        
        if (status==1) {
            NSString *start = [ac.start_time componentsSeparatedByString:@" "][1];
            cell.statusLabel.text = [NSString stringWithFormat:@"本场抽奖将于 %@ 开始",start];
            [cell.statusLabel setTextColor:[Config getTintColor]];
        } else if(status==2){
            cell.statusLabel.text = [NSString stringWithFormat:@"抽奖进行中!"];
            [cell.statusLabel setTextColor:[Config getTintColor]];
        }else if(status==3){
            cell.statusLabel.text = [NSString stringWithFormat:@"本场抽奖已结束!"];
            [cell.statusLabel setTextColor:[UIColor darkGrayColor]];
        }
        return cell;
    } else if(section==1){
        
        if (status==1) {
            UITableViewCell* cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"default"];
            cell.textLabel.text = @"编辑活动";
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            return cell;
        } else {
            if (!row) {
                UITableViewCell* cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:@"cellValue1"];
                cell.textLabel.text = ac.prize[section-1].name;
                [cell.textLabel setTextColor:[Config getTintColor]];
                return cell;
            }else{
                if (ac.prize.count) {
                    GameResultCell* cell = (GameResultCell*)[tableView dequeueReusableCellWithIdentifier:@"GameResultCell"];
                    PriceCategory* price = ac.prize[section-1];
                    Person* person = price.lucky[row-1];
                    cell.firstNameLabel.text = [person.user_name substringToIndex:1];
                    cell.nameLabel.text = person.user_name;
                    
                    return cell;
                }
                return [[UITableViewCell alloc]init];
            }
        }

    }else{
        
        if (!row) {
            UITableViewCell* cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:@"cellValue1"];
            cell.textLabel.text = ac.prize[section-1].name;
            [cell.textLabel setTextColor:[Config getTintColor]];
            return cell;
        }else{
            if (ac.prize.count) {
                GameResultCell* cell = (GameResultCell*)[tableView dequeueReusableCellWithIdentifier:@"GameResultCell"];
                PriceCategory* price = ac.prize[section-1];
                Person* person = price.lucky[row-1];
                cell.firstNameLabel.text = [person.user_name substringToIndex:1];
                cell.nameLabel.text = person.user_name;
                
                return cell;
            }
            return [[UITableViewCell alloc]init];
        }

    }
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([segue.identifier isEqualToString:@"toQRCodeVC"]) {
        
        QRCodeVC* vc = (QRCodeVC*)segue.destinationViewController;
        vc.acid = self.acid;
        
    }else if([segue.identifier isEqualToString:@"toEdit"]){
        
        GameSettingVC* vc = (GameSettingVC*)segue.destinationViewController;
        vc.activity = ac;
        vc.isUpdate = YES;
        
    }
}

- (IBAction)back:(id)sender
{
    [self.navigationController popToRootViewControllerAnimated:YES];
}



@end
