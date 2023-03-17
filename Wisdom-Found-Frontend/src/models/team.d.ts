import {UserType} from "./user";

export type TeamType = {
    id: number;
    name: string;
    description: string;
    // 表示可有可无
    expireTime?: Date;
    maxNum: number;
    password?: string;
    status: number;
    createTime: Date;
    updateTime: Date;
    creatrUser?: UserType;
    hasJoinNum?: number;
};