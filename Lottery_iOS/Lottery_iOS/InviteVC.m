//
//  InviteVC.m
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/11.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import "InviteVC.h"
#import "Config.h"
#import "CellWithImgAndIndicator.h"
#import "InviteCell.h"

@interface InviteVC ()

@property (strong, nonatomic) IBOutlet UISearchBar *searchBar;
@property (strong, nonatomic) NSMutableArray<Person*> *candidates;
@property (strong, nonatomic) NSMutableArray<Person*> *personsResult;

@end

@implementation InviteVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.tableView.tableFooterView = [Config getTableViewFooter];
    
    self.candidates = [[NSMutableArray alloc]init];
    self.personsResult = [[NSMutableArray alloc]init];
    
    NSArray<NSString*>* ids = [[Config getInfoPlistDict] objectForKey:@"user_ids"];
    NSArray<NSString*>* names = [[Config getInfoPlistDict] objectForKey:@"user_names"];
    for (int i=0; i<ids.count; i++) {
        Person* person = [[Person alloc]init];
        person.user_id = ids[i];
        person.user_name = names[i];
        [_candidates addObject:person];
    }
    
    [self.tableView reloadData];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 3;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (!section) {
        return 1;
    } else if(section==2){
        return _candidates.count;
    } else {
        return 1;
    }
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 56.0;
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
        CellWithImgAndIndicator* cell = (CellWithImgAndIndicator*)[tableView dequeueReusableCellWithIdentifier:@"CellWithImgAndIndicator"];
        return cell;
    } else if(section==2){
        InviteCell* cell = (InviteCell*)[tableView dequeueReusableCellWithIdentifier:@"InviteCell"];
        if (_candidates.count) {
            Person* person = self.candidates[row];
            cell.nameLabel.text = person.user_name;
            cell.firstNameLabel.text = [person.user_name substringToIndex:1];
        }
        return cell;
    }else{
        InviteCell* cell = (InviteCell*)[tableView dequeueReusableCellWithIdentifier:@"InviteCell"];
        cell.nameLabel.text = @"全选";
        cell.firstNameLabel.text = @"全";
        return cell;
    }
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSInteger section = indexPath.section;
    NSInteger row = indexPath.row;
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    if (section==2) {
        
        InviteCell* cell = (InviteCell*)[tableView cellForRowAtIndexPath:indexPath];
        if (!cell.tag) {
            [cell.icon setImage:[UIImage imageNamed:@"selected"]];
            cell.tag = 1;
            [self.personsResult addObject:self.candidates[row]];
        } else {
            [cell.icon setImage:[UIImage imageNamed:@"unselected"]];
            cell.tag = 0;
            [self.personsResult removeObject:self.candidates[row]];
        }
        
    }else if(section==1){
        InviteCell* cell = (InviteCell*)[tableView cellForRowAtIndexPath:indexPath];
        
        if (!cell.tag) {
            
            cell.tag=1;
            [cell.icon setImage:[UIImage imageNamed:@"selected"]];
            
            for (int i=0; i<_candidates.count; i++) {
                InviteCell* personcell = (InviteCell*)[tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:i inSection:2]];
                [personcell.icon setImage:[UIImage imageNamed:@"selected"]];
                personcell.tag=1;
                self.personsResult = [_candidates mutableCopy];
            }
            
        } else {
            
            cell.tag=0;
            [cell.icon setImage:[UIImage imageNamed:@"unselected"]];
            for (int i=0; i<_candidates.count; i++) {
                InviteCell* personCell = (InviteCell*)[tableView cellForRowAtIndexPath:[NSIndexPath indexPathForRow:i inSection:2]];
                [personCell.icon setImage:[UIImage imageNamed:@"unselected"]];
                personCell.tag=0;
            }
            [self.personsResult removeAllObjects];
        }

    }
}

-(void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    self.personsBlock(self.personsResult);
}


- (IBAction)selectDone:(id)sender
{
    
}



@end
