//
//  Activity+Request.m
//  Lottery_iOS
//
//  Created by 赵磊 on 16/8/14.
//  Copyright © 2016年 赵磊. All rights reserved.
//

#import "Activity+Request.h"

@implementation Activity (Request)

+(void)createActivityWithReverseToken:(NSString*)reverse_token reverse_id:(NSString*)reverse_id params:(NSDictionary*)params successBlock:(SuccessBlock)successBlock failureBlock:(FailureBlock)failureBlock
{
    NSString* url = [NSString stringWithFormat:@"%@/activity/create.do",[Config getMainURL]];
    AFHTTPSessionManager* manager =[Config getSessionManager];
    [manager.requestSerializer setValue:reverse_token forHTTPHeaderField:@"reverse_token"];
    [manager.requestSerializer setValue:reverse_id forHTTPHeaderField:@"reverse_id"];
    [manager POST:url parameters:params progress:^(NSProgress * _Nonnull uploadProgress) {
        
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
       
        successBlock(responseObject[@"data"]);
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        failureBlock(error);
        
    }];
}
+(void)fetchJoinedLotteriesWithReverseToken:(NSString*)reverse_token reverse_id:(NSString*)reverse_id user_id:(NSString*)user_id successBlock:(SuccessBlock)successBlock failureBlock:(FailureBlock)failureBlock
{
    NSString* url = [NSString stringWithFormat:@"%@/activity/myjoin.do",[Config getMainURL]];
    AFHTTPSessionManager* manager =[Config getSessionManager];
    [manager.requestSerializer setValue:reverse_token forHTTPHeaderField:@"reverse_token"];
    [manager.requestSerializer setValue:reverse_id forHTTPHeaderField:@"reverse_id"];
    [manager POST:url parameters:@{@"user_id":user_id} progress:^(NSProgress * _Nonnull uploadProgress) {
        
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
       
        successBlock(responseObject[@"data"]);
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        failureBlock(error);
        
    }];
}

+(void)fetchSendLotteriesWithReverseToken:(NSString*)reverse_token reverse_id:(NSString*)reverse_id user_id:(NSString*)user_id successBlock:(SuccessBlock)successBlock failureBlock:(FailureBlock)failureBlock
{
    NSString* url = [NSString stringWithFormat:@"%@/activity/myown.do",[Config getMainURL]];
    AFHTTPSessionManager* manager =[Config getSessionManager];
    [manager.requestSerializer setValue:reverse_token forHTTPHeaderField:@"reverse_token"];
    [manager.requestSerializer setValue:reverse_id forHTTPHeaderField:@"reverse_id"];
    [manager POST:url parameters:@{@"user_id":user_id} progress:^(NSProgress * _Nonnull uploadProgress) {
        
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        successBlock(responseObject[@"data"]);
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        failureBlock(error);
        
    }];
}

+(void)fetchActivityResultWithReverseToken:(NSString*)reverse_token reverse_id:(NSString*)reverse_id activity_id:(NSString*)activity_id successBlock:(SuccessBlock)successBlock failureBlock:(FailureBlock)failureBlock
{
    NSString* url = [NSString stringWithFormat:@"%@/activity/result.do",[Config getMainURL]];
    AFHTTPSessionManager* manager =[Config getSessionManager];
    [manager.requestSerializer setValue:reverse_token forHTTPHeaderField:@"reverse_token"];
    [manager.requestSerializer setValue:reverse_id forHTTPHeaderField:@"reverse_id"];
    [manager POST:url parameters:@{@"activity_id":[NSNumber numberWithInt:[activity_id intValue]]} progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        successBlock(responseObject[@"data"]);
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        failureBlock(error);
        
    }];
}

+(void)fecthMyResultWithReverseToken:(NSString*)reverse_token reverse_id:(NSString*)reverse_id params:(NSDictionary*)params successBlock:(SuccessBlock)successBlock failureBlock:(FailureBlock)failureBlock
{
    NSString* url = [NSString stringWithFormat:@"%@/activity/join.do",[Config getMainURL]];
    AFHTTPSessionManager* manager =[Config getSessionManager];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:reverse_token forHTTPHeaderField:@"reverse_token"];
    [manager.requestSerializer setValue:reverse_id forHTTPHeaderField:@"reverse_id"];
    [manager POST:url parameters:params progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        successBlock(responseObject);
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        failureBlock(error);
        
    }];
}

+(void)fetchActivityInfoWithReverseToken:(NSString*)reverse_token reverse_id:(NSString*)reverse_id  activity_id:(NSString*)activity_id successBlock:(SuccessBlock)successBlock failureBlock:(FailureBlock)failureBlock
{
    NSString* url = [NSString stringWithFormat:@"%@/activity/get.do",[Config getMainURL]];
    AFHTTPSessionManager* manager =[Config getSessionManager];
    [manager.requestSerializer setValue:reverse_token forHTTPHeaderField:@"reverse_token"];
    [manager.requestSerializer setValue:reverse_id forHTTPHeaderField:@"reverse_id"];
    [manager POST:url parameters:@{@"activity_id":[NSNumber numberWithInt:[activity_id intValue]]} progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        successBlock(responseObject[@"data"]);
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        failureBlock(error);
        
    }];
}

+(void)updateActivityInfoWithReverseToken:(NSString*)reverse_token reverse_id:(NSString*)reverse_id params:(NSDictionary*)params successBlock:(SuccessBlock)successBlock failureBlock:(FailureBlock)failureBlock
{
    NSString* url = [NSString stringWithFormat:@"%@/activity/update.do",[Config getMainURL]];
    AFHTTPSessionManager* manager =[Config getSessionManager];
    [manager.requestSerializer setValue:reverse_token forHTTPHeaderField:@"reverse_token"];
    [manager.requestSerializer setValue:reverse_id forHTTPHeaderField:@"reverse_id"];
    [manager POST:url parameters:params progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        successBlock(responseObject[@"data"]);
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        failureBlock(error);
        
    }];
}

+(void)fetchRewardsWithReverseToken:(NSString *)reverse_token reverse_id:(NSString *)reverse_id activity_id:(NSString*)activity_id successBlock:(SuccessBlock)successBlock failureBlock:(FailureBlock)failureBlock
{
    NSString* url = [NSString stringWithFormat:@"%@/reward/rewardOfActivity.do",[Config getMainURL]];
    AFHTTPSessionManager* manager =[Config getSessionManager];
    [manager.requestSerializer setValue:reverse_token forHTTPHeaderField:@"reverse_token"];
    [manager.requestSerializer setValue:reverse_id forHTTPHeaderField:@"reverse_id"];
    [manager POST:url parameters:@{@"activity_id":activity_id} progress:^(NSProgress * _Nonnull uploadProgress) {
        
    } success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        successBlock(responseObject);
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        
        failureBlock(error);
        
    }];
}

@end
