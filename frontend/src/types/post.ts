export type createPost = {
    
    title: string;
    content: string;
    introduction: string;
    deadLine : string;
    postStatus :  "ING" | "CLOSED";
    pinStatus : "PINNED" | "NOT_PINNED";
    recruitCount: number;       
};

export type Post = {
    postId: number;
    title: string;
    introduction: string;
    content: string;
    deadLine : string;
    createDate : string;
    modifyDate : string;
    postStatus :  "ING" | "CLOSED";
    pinStatus : "PINNED" | "NOT_PINNED";
    recruitCount: number;     
    nickName : string;

}