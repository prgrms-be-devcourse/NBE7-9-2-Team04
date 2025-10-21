export type CreatePost = {
    
    title: string;
    content: string;
    introduction: string;
    deadline : string;
    status :  "ING" | "CLOSED";
    pinStatus? : "PINNED" | "NOT_PINNED";
    recruitCount: number;     
    categoryType: "PROJECT" | "STUDY";  
};

export type Post = {
    postId: number;
    title: string;
    introduction: string;
    content: string;
    deadline : string;
    createDate : string;
    modifyDate : string;
    status :  "ING" | "CLOSED";
    pinStatus : "PINNED" | "NOT_PINNED";
    recruitCount: number;     
    nickName : string;
    categoryType: "PROJECT" | "STUDY";

}