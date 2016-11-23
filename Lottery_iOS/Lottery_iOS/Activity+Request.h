//
//  Activity+Request.h
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/14.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import "Activity.h"
#import "Config.h"
#import <AFNetworking/AFNetworking.h>

@interface Activity (Request)

+(void)createActivityWithReverseToken:(NSString*)reverse_token reverse_id:(NSString*)reverse_id params:(NSDictionary*)params successBlock:(SuccessBlock)successBlock failureBlock:(FailureBlock)failureBlock;

+(void)fetchJoinedLotteriesWithReverseToken:(NSString*)reverse_token reverse_id:(NSString*)reverse_id user_id:(NSString*)user_id successBlock:(SuccessBlock)successBlock failureBlock:(FailureBlock)failureBlock;

+(void)fetchSendLotteriesWithReverseToken:(NSString*)reverse_token reverse_id:(NSString*)reverse_id user_id:(NSString*)user_id successBlock:(SuccessBlock)successBlock failureBlock:(FailureBlock)failureBlock;

+(void)fetchActivityResultWithReverseToken:(NSString*)reverse_token reverse_id:(NSString*)reverse_id activity_id:(NSString*)activity_id successBlock:(SuccessBlock)successBlock failureBlock:(FailureBlock)failureBlock;

+(void)fetchActivityInfoWithReverseToken:(NSString*)reverse_token reverse_id:(NSString*)reverse_id  activity_id:(NSString*)activity_id successBlock:(SuccessBlock)successBlock failureBlock:(FailureBlock)failureBlock;

+(void)updateActivityInfoWithReverseToken:(NSString*)reverse_token reverse_id:(NSString*)reverse_id params:(NSDictionary*)params successBlock:(SuccessBlock)successBlock failureBlock:(FailureBlock)failureBlock;

+(void)fecthMyResultWithReverseToken:(NSString*)reverse_token reverse_id:(NSString*)reverse_id params:(NSDictionary*)params successBlock:(SuccessBlock)successBlock failureBlock:(FailureBlock)failureBlock;

+(void)fetchRewardsWithReverseToken:(NSString *)reverse_token reverse_id:(NSString *)reverse_id activity_id:(NSString*)activity_id successBlock:(SuccessBlock)successBlock failureBlock:(FailureBlock)failureBlock;

@end
