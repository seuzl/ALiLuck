//
//  ConnectManager.m
//  testMultipeerConnectivity
//
//  Created by 赵磊 on 16/8/13.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import "ConnectManager.h"

@interface ConnectManager()
{
    NSString* connectType;
    MCPeerID* myPeerID;
    MCNearbyServiceAdvertiser *connectAdvertiser;
    MCNearbyServiceBrowser *connectBrowser;
}
@end

@implementation ConnectManager

- (instancetype)init
{
    
    NSString* deviceName = [[UIDevice currentDevice] name];
    
    connectType = @"aliluck";
    myPeerID = [[MCPeerID alloc]initWithDisplayName:deviceName];
    
    self = [super init];
    if (self) {
        
        connectAdvertiser = [[MCNearbyServiceAdvertiser alloc]initWithPeer:myPeerID discoveryInfo:nil serviceType:connectType];
        connectBrowser = [[MCNearbyServiceBrowser alloc]initWithPeer:myPeerID serviceType:connectType];
        
        self.session = [[MCSession alloc]initWithPeer:myPeerID securityIdentity:nil encryptionPreference:MCEncryptionRequired];
        self.session.delegate = self;
        
        connectAdvertiser.delegate = self;
        [self startAdvertiser];
        
        connectBrowser.delegate = self;
        [self startBrowser];
        
        self.askList = [[NSMutableArray<MCPeerID*> alloc]init];
        self.helpList = [[NSMutableArray<MCPeerID*> alloc]init];

    }
    return self;
}

// A问B代发
-(void)askOthersHelpSendData:(NSDictionary*)dictionary
{
    if (self.session.connectedPeers.count) {
        int otherIndex = arc4random()%self.session.connectedPeers.count;
        NSArray* arr = [NSArray arrayWithObject:[_session.connectedPeers objectAtIndex:otherIndex]];
        
        [self.askList addObject:[arr objectAtIndex:0]];
        
        NSLog(@"A问B代发:%@",self.askList[0]);
        
        [self.session sendData:[NSKeyedArchiver archivedDataWithRootObject:dictionary] toPeers:arr withMode:MCSessionSendDataReliable error:nil];
    }
}

//B将服务器返回告知A
-(void)responseToAskWithData:(NSDictionary*)dictionary toPeer:(MCPeerID*)peerID
{
    
    if (self.session.connectedPeers.count) {
    
        NSLog(@"B帮A发后，服务器返回给A:%@",peerID);
        
        NSArray* arr = [NSArray arrayWithObject:peerID];
        
        [self.helpList removeObject:peerID];
        
        [self.session sendData:[NSKeyedArchiver archivedDataWithRootObject:dictionary] toPeers:arr withMode:MCSessionSendDataReliable error:nil];
    }
}

-(void)dealloc
{
    [connectAdvertiser stopAdvertisingPeer];
    [connectBrowser stopBrowsingForPeers];
}

-(void)startAdvertiser
{
    [connectAdvertiser startAdvertisingPeer];
}

-(void)startBrowser
{
    [connectBrowser startBrowsingForPeers];
}

# pragma MCNearbyServiceAdvertiserDelegate

-(void)advertiser:(MCNearbyServiceAdvertiser *)advertiser didNotStartAdvertisingPeer:(NSError *)error
{
    NSLog(@"didNotStartAdvertisingPeer:%@",error);
}

-(void)advertiser:(MCNearbyServiceAdvertiser *)advertiser didReceiveInvitationFromPeer:(MCPeerID *)peerID withContext:(NSData *)context invitationHandler:(void (^)(BOOL, MCSession * _Nonnull))invitationHandler
{
    NSLog(@"didReceiveInvitationFromPeer:%@",peerID);
    invitationHandler(true,self.session);
}


#pragma MCNearbyServiceBrowserDelegate

-(void)browser:(MCNearbyServiceBrowser *)browser lostPeer:(MCPeerID *)peerID
{
    NSLog(@"lostPeer: %@",peerID);
}

-(void)browser:(MCNearbyServiceBrowser *)browser didNotStartBrowsingForPeers:(NSError *)error
{
    NSLog(@"didNotStartBrowsingForPeers: %@",error);
}

-(void)browser:(MCNearbyServiceBrowser *)browser foundPeer:(MCPeerID *)peerID withDiscoveryInfo:(NSDictionary<NSString *,NSString *> *)info
{
    NSLog(@"foundPeer:%@",peerID);
    NSLog(@"invitePeer:%@",peerID);
    
    [browser invitePeer:peerID toSession:self.session withContext:nil timeout:10];
}

#pragma MCSessionDelegate

-(void)session:(MCSession *)session peer:(MCPeerID *)peerID didChangeState:(MCSessionState)state
{
    NSLog(@"peer %@ didChangeState:%@",peerID,[self stringOfState:state]);
    
    NSLog(@"self.session.connectedPeers:%@",self.session.connectedPeers);
    
    NSMutableArray* devices = [[NSMutableArray alloc]init];
    
    for (MCPeerID* peerid in self.session.connectedPeers) {
        [devices addObject:peerid];
    }
    
    [_delegate connectedDevicesChangedWithManager:self connectedDevices:devices];
}

-(void)session:(MCSession *)session didReceiveData:(NSData *)data fromPeer:(MCPeerID *)peerID
{
    NSLog(@"didReceiveData:%ld bytes",(unsigned long)[data length]);
    
    NSLog(@"判断来的数据是哪种：self.askList:%@,peerID:%@",self.askList,peerID);
    
    if ([self isInAskList:peerID]) {
        
        [self.askList removeObject:peerID];
        
        NSDictionary* dic = (NSDictionary*)[NSKeyedUnarchiver unarchiveObjectWithData:data];
        NSLog(@"A收到了来自B的反馈:%@",dic);
        
        [self.delegate getFeedBackFromWhoHelpMeWithData:dic];
        
    } else {
        
//        来自A的请求
        [self.helpList addObject:peerID];
        
        NSDictionary* dic = (NSDictionary*)[NSKeyedUnarchiver unarchiveObjectWithData:data];
        NSLog(@"B收到了来自A的请求:%@",dic);
        
        [self.delegate helpSendOthersDataWithManager:self data:dic peerID:peerID];
        
    }
}

-(void)session:(MCSession *)session didReceiveStream:(NSInputStream *)stream withName:(NSString *)streamName fromPeer:(MCPeerID *)peerID
{
    NSLog(@"didReceiveStream");
}

-(void)session:(MCSession *)session didFinishReceivingResourceWithName:(NSString *)resourceName fromPeer:(MCPeerID *)peerID atURL:(NSURL *)localURL withError:(NSError *)error
{
    NSLog(@"didFinishReceivingResourceWithName");
}

-(void)session:(MCSession *)session didStartReceivingResourceWithName:(NSString *)resourceName fromPeer:(MCPeerID *)peerID withProgress:(NSProgress *)progress
{
    NSLog(@"didStartReceivingResourceWithName");
}

-(NSString*)stringOfState:(MCSessionState)state
{
    switch (state) {
        case MCSessionStateNotConnected:
            return @"NotConnected";
            break;
            
        case MCSessionStateConnected:
            return @"Connected";
            break;
            
        case MCSessionStateConnecting:
            return @"Connecting";
            break;
            
        default:
            return @"UnknownState";
            break;
    }
}

-(BOOL)isInAskList:(MCPeerID*)peeID
{
    for (MCPeerID* peerid in self.askList) {
        if ([peeID isEqual:peerid]) {
            return YES;
        }
    }
    return NO;
}

@end
