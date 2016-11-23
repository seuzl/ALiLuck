//
//  ConnectManager.h
//  testMultipeerConnectivity
//
//  Created by 赵磊 on 16/8/13.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MultipeerConnectivity/MultipeerConnectivity.h>

@class ConnectManager;

@protocol ConnectManagerDelegate <NSObject>

-(void)connectedDevicesChangedWithManager:(ConnectManager*)manager connectedDevices:(NSMutableArray<NSString*>*)devices;
-(void)helpSendOthersDataWithManager:(ConnectManager*)manager data:(NSDictionary*)data peerID:(MCPeerID*)askedPerrID;
-(void)getFeedBackFromWhoHelpMeWithData:(NSDictionary*)dic;

@end

@interface ConnectManager : NSObject<MCNearbyServiceBrowserDelegate,MCNearbyServiceAdvertiserDelegate,MCSessionDelegate>

@property (nonatomic,strong) MCSession* session;
@property (nonatomic,strong) id<ConnectManagerDelegate> delegate;
@property (nonatomic,strong) NSMutableArray<MCPeerID*>* helpList;
@property (nonatomic,strong) NSMutableArray<MCPeerID*>* askList;

-(void)startAdvertiser;
-(void)startBrowser;
-(void)askOthersHelpSendData:(NSDictionary*)dictionary;
-(void)responseToAskWithData:(NSDictionary*)dictionary toPeer:(MCPeerID*)peerID;


@end




