//
//  GameSettingVC.m
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/11.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import "GameSettingVC.h"
#import "Config.h"
#import "CellWithTwoLabels.h"
#import "CellWithLabelAndBtn.h"
#import "CellWithTwoTextFields.h"
#import "CellWithLabelAndTextField.h"
#import "XMRTimePiker.h"
#import "PriceCategory.h"
#import "InviteVC.h"
#import "GameResultVC.h"
#import "Activity+Request.h"

#define selfHeight [UIScreen mainScreen].bounds.size.height

@interface GameSettingVC ()<XMRTimePikerDelegate>

@end

@implementation GameSettingVC

- (void)viewDidLoad {
    
    [super viewDidLoad];
    self.tableView.tableFooterView = [Config getTableViewFooter];
    
    NSDictionary* dic = @{
                          @"creator_id":[Config getUserID],
                          @"title":@"请填写",
                          @"start_time":@"请选择",
                          @"end_time":@"",
                          @"participants":@[],
                          @"longitude":@0.0,
                          @"latitude":@0.0,
                          @"limit":@0,
                          @"prize":@[@{@"name":@"",@"number":@1}]
                          };
    
    if (!self.activity) {
        self.activity = [Activity mj_objectWithKeyValues:dic];
    }
    
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 5;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (section==2) {
        return self.activity.prize.count+1;
    }else{
        return 1;
    }
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 44.0;
}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return [Config getSectionHeaderHeight];
}

-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSInteger section = indexPath.section;
    NSInteger row = indexPath.row;
    
    if (!section) {
//        title
        CellWithLabelAndTextField* cell = (CellWithLabelAndTextField*)[tableView dequeueReusableCellWithIdentifier:@"CellWithLabelAndTextField"];
        cell.label.text = @"活动名称";
        
        if ([self.activity.title isEqualToString:@"请填写"]) {
            cell.textField.placeholder = @"请填写";
        } else {
            cell.textField.text = self.activity.title;
        }
        return cell;
    
    } else if (section==1){
        
//        start_time
        CellWithTwoLabels* cell = (CellWithTwoLabels*)[tableView dequeueReusableCellWithIdentifier:@"CellWithTwoLabels"];
        cell.label0.text = @"抽奖时间段";
        if ([self.activity.start_time isEqualToString:@"请选择"]) {
            cell.label1.text = self.activity.start_time;
        } else {
            
            NSString* start = [self.activity.start_time componentsSeparatedByString:@" "][1];
            NSString* end = [self.activity.end_time componentsSeparatedByString:@" "][1];
            cell.label1.text = [NSString stringWithFormat:@"%@ － %@",start,end];
        }
        
        return cell;
        
    } else if (section==2){
        
//      奖项&中奖人数
        if (!row) {
            CellWithLabelAndBtn* cell = (CellWithLabelAndBtn*)[tableView dequeueReusableCellWithIdentifier:@"CellWithLabelAndBtn"];
            cell.addBtn.tag=0;
            return cell;
        } else {
            CellWithTwoTextFields* cell = (CellWithTwoTextFields*)[tableView dequeueReusableCellWithIdentifier:@"CellWithTwoTextFields"];
            PriceCategory* prize  = self.activity.prize[row-1];
            if (prize.name && ![prize.name isEqualToString:@""]){
                cell.textField0.text = prize.name;
                cell.textField1.text = [NSString stringWithFormat:@"%ld",prize.number];
            }
            cell.add.tag=row;
            cell.minus.tag=row;
            return cell;
        }

        
    } else if(section==3){
        
            CellWithTwoLabels* cell = (CellWithTwoLabels*)[tableView dequeueReusableCellWithIdentifier:@"CellWithTwoLabels"];
            cell.label0.text = @"限制抽奖者最远距离";
            [cell.label0 sizeToFit];
            if (!self.activity.limit) {
                cell.label1.text = @"不开启";
            } else {
                cell.label1.text = [NSString stringWithFormat:@"%ld",(long)self.activity.limit];
            }
            return cell;
            
    }else{
        CellWithLabelAndBtn* cell = (CellWithLabelAndBtn*)[tableView dequeueReusableCellWithIdentifier:@"CellWithLabelAndBtn"];
        cell.label.text = @"邀请人员参加";
        cell.addBtn.tag=1;
        return cell;
    }
    
    
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSInteger section = indexPath.section;
    NSInteger row = indexPath.row;
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    if (section==1) {
        
        XMRTimePiker *piker=[[XMRTimePiker alloc]init];
        
        piker.delegate=self;
        
        [piker showTime];
        
    } else if(section==2){
        
        if (!row) {
            PriceCategory* category = [[PriceCategory alloc]init];
            category.number = 1;
            [self.activity.prize addObject:category];
            [self.tableView insertRowsAtIndexPaths:[NSArray arrayWithObject:[NSIndexPath indexPathForRow:self.activity.prize.count inSection:2]] withRowAnimation:UITableViewRowAnimationBottom];
        }
    } else if(section==3){
        UIAlertController* alert = [UIAlertController alertControllerWithTitle:@"抽奖者距离我最远" message:nil preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction* action0 = [UIAlertAction actionWithTitle:@"不开启" style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
            
            self.activity.limit = 0;
            [self.tableView reloadRowsAtIndexPaths:[NSArray arrayWithObject:[NSIndexPath indexPathForRow:0 inSection:3]] withRowAnimation:UITableViewRowAnimationFade];
            
        }];
        UIAlertAction* action1 = [UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
            
            self.activity.limit = [alert.textFields[0].text intValue];
            self.activity.latitude = [Config getLatitude];
            self.activity.longitude = [Config getLongitude];
            [self.tableView reloadRowsAtIndexPaths:[NSArray arrayWithObject:[NSIndexPath indexPathForRow:0 inSection:3]] withRowAnimation:UITableViewRowAnimationFade];
            
        }];

        [alert addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
            
            textField.placeholder = @"单位：米";
            textField.keyboardType = UIKeyboardTypeNumberPad;
            
            
        }];
        [alert addAction:action0];
        [alert addAction:action1];
        
        [self presentViewController:alert animated:YES completion:nil];
        
    }else if(section==4){
//        invite
        [self performSegueWithIdentifier:@"toInviteVC" sender:nil];
    }
}

-(BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSInteger section = indexPath.section;
    NSInteger row = indexPath.row;
    if (section==2 && row>1) {
        return YES;
    } else {
        return NO;
    }
}

- (UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return UITableViewCellEditingStyleDelete;
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath{
    if (editingStyle ==UITableViewCellEditingStyleDelete) {
        
        if (indexPath.row-1<[self.activity.prize count]) {
            
            [self.activity.prize removeObjectAtIndex:indexPath.row-1];
            
            [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationLeft];
        }
        
    }
    
}

- (IBAction)done:(UIBarButtonItem *)sender
{
    //        title
    NSIndexPath* indexPath = [NSIndexPath indexPathForRow:0 inSection:0];
    CellWithLabelAndTextField* cell = (CellWithLabelAndTextField*)[self.tableView cellForRowAtIndexPath:indexPath];
    
    if (![cell.textField.text isEqualToString:@""] && cell.textField.text) {
        
        self.activity.title = cell.textField.text;
        
        if (self.activity.prize.count) {
            
//            奖项
            NSUInteger i = self.activity.prize.count;
            NSUInteger j = 0;
            NSUInteger totalPrizeCount = 0;
            for (j = 0; j<i; j++) {
                NSIndexPath* path1 = [NSIndexPath indexPathForRow:j+1 inSection:2];
                CellWithTwoTextFields* cell2 = (CellWithTwoTextFields*)[self.tableView cellForRowAtIndexPath:path1];
                NSString* name = cell2.textField0.text;
                NSString* numberstr = cell2.textField1.text;
                if ([name isEqualToString:@""] || !name || [numberstr isEqualToString:@""] || !numberstr) {
                    [Config showErrorHUDwithStatus:@"奖项信息不完整!"];
                    break;
                } else {
                    self.activity.prize[j].name = name;
                    if ([numberstr intValue]) {
                        self.activity.prize[j].number = [numberstr intValue];
                        totalPrizeCount+=[numberstr intValue];
                    } else {
                        [Config showErrorHUDwithStatus:@"中奖人数不可为0!"];
                        break;
                    }
                }
            }
            if (j==i) {
                if (self.activity.participants) {
                    if (self.activity.participants.count>=totalPrizeCount) {
                        
                        if (!self.isUpdate) {
                            
                            [Activity createActivityWithReverseToken:nil reverse_id:nil params:[self.activity mj_keyValues] successBlock:^(id returnValue) {
                                
                                [Config showSuccessHUDwithStatus:@"活动创建成功!"];
                                GameResultVC* vc = (GameResultVC*)[Config getVCFromSb:@"GameResultVC"];
                                vc.acid = [NSString stringWithFormat:@"%d",[returnValue[@"activity_id"] intValue]];
                                [self.navigationController pushViewController:vc animated:YES];
                                
                            } failureBlock:^(NSError *error) {
                                [Config showErrorHUDwithStatus:@"服务器出错啦!"];
                                NSLog(@"创建活动出错!%@",error);
                            }];

                            
                        } else {
                            
                            [Activity updateActivityInfoWithReverseToken:nil reverse_id:nil params:[self.activity mj_keyValues] successBlock:^(id returnValue){
                               
                                [Config showSuccessHUDwithStatus:@"更新成功!"];
                                GameResultVC* vc = (GameResultVC*)[Config getVCFromSb:@"GameResultVC"];
                                vc.acid = [NSString stringWithFormat:@"%d",[returnValue[@"activity_id"] intValue]];
                                [self.navigationController pushViewController:vc animated:YES];
                                
                            } failureBlock:^(NSError *error) {
                                
                                [Config showErrorHUDwithStatus:@"更新失败!"];
                                NSLog(@"更新活动失败!%@",error);
                                
                            }];
                        }
                        
                        
                    } else {
                        [Config showErrorHUDwithStatus:@"中奖人数不可多于抽奖人数!"];
                    }
                } else {
                    [Config showErrorHUDwithStatus:@"请设置抽奖人员!"];
                }
            }
        } else {
            [Config showErrorHUDwithStatus:@"请设置至少一个奖项!"];
        }
    } else {
        [Config showErrorHUDwithStatus:@"请填写活动名称!"];
    }
    
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if([segue.identifier isEqualToString:@"toInviteVC"]){
    
        InviteVC* invitevc = (InviteVC*)segue.destinationViewController;
        invitevc.personsBlock = ^(NSMutableArray<Person*>*ids){
            self.activity.participants = ids;
        };
    }
}

-(void)XMSelectTimesViewSetOneLeft:(NSString *)oneLeft andOneRight:(NSString *)oneRight andTowLeft:(NSString *)towLeft andTowRight:(NSString *)towRight{
    
    NSString* yearMon = [[Config getCurrentDateString] componentsSeparatedByString:@" "][0];
    self.activity.start_time = [NSString stringWithFormat:@"%@ %@:%@",yearMon,oneLeft,oneRight];
    self.activity.end_time = [NSString stringWithFormat:@"%@ %@:%@",yearMon,towLeft,towRight];
    [self.tableView reloadData];
}

- (IBAction)addPriceCategory:(UIButton *)sender
{
    if (!sender.tag) {
        PriceCategory* category = [[PriceCategory alloc]init];
        category.number = 1;
        [self.activity.prize addObject:category];
        [self.tableView insertRowsAtIndexPaths:[NSArray arrayWithObject:[NSIndexPath indexPathForRow:self.activity.prize.count inSection:2]] withRowAnimation:UITableViewRowAnimationBottom];
    } else {
         [self performSegueWithIdentifier:@"toInviteVC" sender:nil];
    }
}

- (IBAction)addPeople:(UIButton *)sender
{
    CellWithTwoTextFields* cell = (CellWithTwoTextFields*)[self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:sender.tag inSection:2]];
    int count = [cell.textField1.text intValue];
    count++;
    cell.textField1.text = [NSString stringWithFormat:@"%d",count];
    
}

- (IBAction)minusPeople:(UIButton *)sender
{
    CellWithTwoTextFields* cell = (CellWithTwoTextFields*)[self.tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:sender.tag inSection:2]];
    int count = [cell.textField1.text intValue];
    if (count<=1) {
        
        [Config showErrorHUDwithStatus:@"人数不可为0"];
        
    } else {
        
        count--;
        cell.textField1.text = [NSString stringWithFormat:@"%d",count];

        
    }
}



@end
