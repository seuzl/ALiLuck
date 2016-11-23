//
//  HomeVC.m
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/10.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import "HomeVC.h"
#import "HomeCell.h"
#import "LBXScanResult.h"
#import "LBXScanWrapper.h"
#import "LBXScanViewStyle.h"
#import "MJRefresh.h"
#import "SubLBXScanViewController.h"
#import "LoginVC.h"
#import "GameVC.h"
#import "GameResultVC.h"

@interface HomeVC ()<UIScrollViewDelegate,UITableViewDelegate,UITableViewDataSource>

@property (strong, nonatomic) IBOutlet UIButton *askBtn;

@property (strong, nonatomic) IBOutlet UIButton *sendBtn;

@property (strong, nonatomic) IBOutlet UIScrollView *bigScrollView;

@property (strong, nonatomic) IBOutlet UITableView *askLotteryTableView;

@property (strong, nonatomic) IBOutlet UITableView *sendLotteryTableView;

@property (strong, nonatomic) IBOutlet UIScrollView *scrollIndicator;

@property (strong, nonatomic) IBOutlet UIView *inScrollIndicator;

@property (strong, nonatomic) UIPageControl *pageCtrl;

@property (strong, nonatomic) NSString* gameurl;

@property (strong, nonatomic) NSString* resultVCid;

@end

@implementation HomeVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setUpPageCtrl];
    [self setUpScrollIndicator];
    [self setUpRefresh];
    
    self.askLotteryTableView.tableFooterView = [Config getTableViewFooter];
    self.sendLotteryTableView.tableFooterView = [Config getTableViewFooter];
    
    self.askArray = [[NSMutableArray alloc] init];
    self.sendArray = [[NSMutableArray alloc] init];
    
    [self fetchResults];
}

-(void)fetchResults
{
    [Activity fetchJoinedLotteriesWithReverseToken:nil reverse_id:nil user_id:[Config getUserID] successBlock:^(id returnValue) {
        
        self.askArray = [Activity mj_objectArrayWithKeyValuesArray:returnValue];
        [self.askLotteryTableView.mj_header endRefreshing];
        [self.askLotteryTableView reloadData];
        
    } failureBlock:^(NSError *error) {
        
        NSLog(@"获取我参加过的抽奖列表失败！%@",error);
        [self.askLotteryTableView.mj_header endRefreshing];
        
    }];
    
    [Activity fetchSendLotteriesWithReverseToken:nil reverse_id:nil user_id:[Config getUserID] successBlock:^(id returnValue) {
        
        self.sendArray = [Activity mj_objectArrayWithKeyValuesArray:returnValue];
        [self.sendLotteryTableView.mj_header endRefreshing];
        [self.sendLotteryTableView reloadData];
        
    } failureBlock:^(NSError *error) {
       
        [self.sendLotteryTableView.mj_header endRefreshing];
        NSLog(@"获取我发起的抽奖列表失败！%@",error);
        
    }];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

-(void)setUpPageCtrl
{
    self.pageCtrl = [[UIPageControl alloc]init];
    self.pageCtrl.numberOfPages = 2;
    self.pageCtrl.currentPage = 0;
    self.pageCtrl.pageIndicatorTintColor = [UIColor clearColor];
    self.pageCtrl.currentPageIndicatorTintColor = [UIColor clearColor];
    self.pageCtrl.enabled = NO;
    [self.bigScrollView addSubview:self.pageCtrl];
    [self.pageCtrl addObserver:self forKeyPath:@"currentPage" options:NSKeyValueObservingOptionNew context:nil];
}

-(void)setUpRefresh
{
    self.askLotteryTableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefreshing_ask)];
//    self.askLotteryTableView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(footerRefreshing_ask)];
    
    self.sendLotteryTableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefreshing_send)];
//    self.sendLotteryTableView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(footerRefreshing_send)];
}

-(void)headerRefreshing_ask
{
    [self fetchResults];
}

-(void)footerRefreshing_ask
{
    
}

-(void)headerRefreshing_send
{
    [self fetchResults];
}

-(void)footerRefreshing_send
{
    
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    if (!self.pageCtrl.currentPage) {
        self.scrollIndicator.contentOffset = CGPointMake(0, 0);
    } else {
        self.scrollIndicator.contentOffset = CGPointMake(-self.view.frame.size.width/2, 0);
    }
}

-(void)setUpScrollIndicator
{
    self.scrollIndicator.contentOffset = CGPointMake(0, 0);
    self.scrollIndicator.pagingEnabled = YES;
}

-(void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    if (scrollView == self.bigScrollView) {
        
        CGPoint offset = scrollView.contentOffset;
        self.scrollIndicator.contentOffset = CGPointMake(-offset.x / 2, offset.y);
        
        int tmpPage = (int)floor((offset.x - self.view.frame.size.width/2)/self.view.frame.size.width)+1;
        self.pageCtrl.currentPage = tmpPage;
    }
}

-(void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary<NSString *,id> *)change context:(void *)context
{
    if ([keyPath isEqualToString:@"currentPage"]) {
        [self tabChangeTo:self.pageCtrl.currentPage];
    }
}

-(void)tabChangeTo:(NSInteger)state
{
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:0.25];
    [UIView setAnimationCurve:UIViewAnimationCurveEaseIn];
    
    if (!state) {
        [self.askBtn setTitleColor:[Config getTintColor] forState:UIControlStateNormal];
        [self.sendBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    } else {
        [self.askBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
        [self.sendBtn setTitleColor:[Config getTintColor] forState:UIControlStateNormal];
    }
    
    [UIView commitAnimations];
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    if (!tableView.tag) {
        return  self.askArray.count;
    } else {
        return  self.sendArray.count;
    }
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}

-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    NSInteger section = indexPath.section;
    
    if (!tableView.tag) {
        
        HomeCell* cell = [tableView dequeueReusableCellWithIdentifier:@"HomeCell"];
        if (self.askArray.count) {
            Activity* act = [self.askArray objectAtIndex:section];
            cell.titleLabel.text = act.title;
            cell.organizerLabel.text = [NSString stringWithFormat:@"发起人：%@",act.creator_name];
            cell.organizedTimeLabel.text = act.create_time;
            
            NSString* start_time = act.start_time;
            NSString* end_time = act.end_time;
            
            int start_com =[Config compareDate:[Config getCurrentDateString] withDate:start_time];
            int end_com = [Config compareDate:[Config getCurrentDateString] withDate:end_time];
            
            if (start_com>=0) {
                if (end_com<=0) {
                    cell.statusLabel.text = [NSString stringWithFormat:@"活动进行中!"];
                    [cell.statusLabel setTextColor:[Config getTintColor]];
                } else {
                    cell.statusLabel.text = [NSString stringWithFormat:@"活动已结束"];
                    [cell.statusLabel setTextColor:[UIColor darkGrayColor]];
                }
            } else {
                NSString *start = [start_time componentsSeparatedByString:@" "][1];
                cell.statusLabel.text = [NSString stringWithFormat:@"%@ 开始",start];
                [cell.statusLabel setTextColor:[Config getTintColor]];
            }
        }
        return cell;
    } else {
        
        HomeCell* cell = [tableView dequeueReusableCellWithIdentifier:@"HomeCell"];
        if (self.sendArray.count) {
            Activity* act = [self.sendArray objectAtIndex:section];
            cell.titleLabel.text = act.title;
            cell.organizedTimeLabel.text = act.create_time;
            
            NSString* start_time = act.start_time;
            NSString* end_time = act.end_time;
            
            int start_com =[Config compareDate:[Config getCurrentDateString] withDate:start_time];
            int end_com = [Config compareDate:[Config getCurrentDateString] withDate:end_time];
            
            if (start_com>=0) {
                if (end_com<=0) {
                    cell.statusLabel.text = [NSString stringWithFormat:@"活动进行中!"];
                    [cell.statusLabel setTextColor:[Config getTintColor]];
                } else {
                    cell.statusLabel.text = [NSString stringWithFormat:@"活动已结束"];
                    [cell.statusLabel setTextColor:[UIColor darkGrayColor]];
                }
            } else {
                NSString *start = [start_time componentsSeparatedByString:@" "][1];
                cell.statusLabel.text = [NSString stringWithFormat:@"%@ 开始",start];
                [cell.statusLabel setTextColor:[Config getTintColor]];
            }
        }
        return cell;
    }

}

//-(void)tableView:(UITableView *)tableView willDisplayCell:(UITableViewCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath
//{
//    CATransform3D rotation = CATransform3DIdentity;
//    rotation = CATransform3DMakeTranslation(0, 50, 20);
//    rotation = CATransform3DScale(rotation, 0.9, 0.9 , 1);
//    rotation.m34 = 1.0/-600;
//    
//    cell.layer.shadowColor = [[UIColor blackColor] CGColor];
//    cell.alpha = 0;
//    cell.layer.transform = rotation;
//    
//    [UIView beginAnimations:@"rotation" context:nil];
//    [UIView setAnimationDuration:0.6];
//    cell.layer.transform = CATransform3DIdentity;
//    cell.alpha = 1;
//    cell.layer.shadowOffset = CGSizeMake(0, 0);
//    [UIView commitAnimations];
//}

-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return [Config getSectionHeaderHeight];
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 120.0;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    NSInteger sec = indexPath.section;
    
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    HomeCell* cell = (HomeCell*)[tableView cellForRowAtIndexPath:indexPath];
    
    if (!tableView.tag) {
        
        if (![cell.statusLabel.text isEqualToString:@"活动进行中!"] && ![cell.statusLabel.text isEqualToString:@"活动已结束"]) {
            [Config showErrorHUDwithStatus:@"活动尚未开始哦!"];
        } else {
            Activity* ac = [self.askArray objectAtIndex:sec];
            
            self.gameurl = [NSString stringWithFormat:@"%@?user_id=%@&access_token=%@&activity_id=%ld&longitude=%f&latitude=%f&reverse_id=%@&reverse_token=%@",[Config getQingWuURL],[Config getUserID],[Config getAccessToken],ac.activity_id,[Config getLongitude],[Config getLatitude],@"",@""];
            
            [self performSegueWithIdentifier:@"toGameVC" sender:nil];
        }
    } else {
        
        Activity* ac = [self.sendArray objectAtIndex:sec];;
        self.resultVCid = [NSString stringWithFormat:@"%ld",ac.activity_id];
        [self performSegueWithIdentifier:@"toGameResultVC" sender:nil];
    }
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([segue.identifier isEqualToString:@"toGameVC"]) {
        
        GameVC* gameVC = (GameVC*)segue.destinationViewController;
        gameVC.gameURL = self.gameurl;

    }else if ([segue.identifier isEqualToString:@"toGameResultVC"]){
        
        GameResultVC* vc = (GameResultVC*)segue.destinationViewController;
        vc.acid = self.resultVCid;
    }
}

- (IBAction)askLoterry:(UIButton *)sender
{
    self.bigScrollView.contentOffset = CGPointMake(0, 0);
}

- (IBAction)sendLottery:(UIButton *)sender
{
    self.bigScrollView.contentOffset = CGPointMake(self.view.frame.size.width, 0);
}

- (IBAction)logout:(id)sender
{
    [Config removeAllObject];
    LoginVC* loginVC = (LoginVC*)[Config getVCFromSb:@"loginVC"];
    [self.navigationController presentViewController:loginVC animated:YES completion:nil];
}

- (IBAction)add:(UIButton *)sender
{
    if (!self.pageCtrl.currentPage) {
        
        //扫一扫加入抽奖
        [self ZhiFuBaoStyle];
        
    } else {
        if ([Config checkConnect]) {
            
            [self performSegueWithIdentifier:@"toGameSetting" sender:nil];
            
        } else {
            [Config showErrorHUDwithStatus:@"无网络，暂不支持创建活动~"];
        }
    }
}

- (void)ZhiFuBaoStyle
{
    LBXScanViewStyle *style = [[LBXScanViewStyle alloc]init];
    style.centerUpOffset = 60;
    style.xScanRetangleOffset = 30;
    
    if ([UIScreen mainScreen].bounds.size.height <= 480 )
    {
        style.centerUpOffset = 40;
        style.xScanRetangleOffset = 20;
    }
    
    
    style.alpa_notRecoginitonArea = 0.6;
    
    style.photoframeAngleStyle = LBXScanViewPhotoframeAngleStyle_Inner;
    style.photoframeLineW = 2.0;
    style.photoframeAngleW = 16;
    style.photoframeAngleH = 16;
    
    style.isNeedShowRetangle = NO;
    
    style.anmiationStyle = LBXScanViewAnimationStyle_NetGrid;
    
    UIImage *imgFullNet = [UIImage imageNamed:@"CodeScan.bundle/qrcode_scan_full_net"];
    
    style.animationImage = imgFullNet;
    
    
    [self openScanVCWithStyle:style];
}

- (void)openScanVCWithStyle:(LBXScanViewStyle*)style
{
    SubLBXScanViewController *vc = [SubLBXScanViewController new];
    vc.style = style;
    [self.navigationController pushViewController:vc animated:YES];
}


@end
