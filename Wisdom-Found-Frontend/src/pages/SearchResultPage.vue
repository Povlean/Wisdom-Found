<template>
  <user-card-list :user-list="userList" />
  <van-empty v-if="!userList || userList.length < 1" description="搜索结果为空"/>
</template>

<script setup>
  import {onMounted, ref} from "vue";
  import myAxios from "../plugins/myAxios.ts";
  import {useRoute} from "vue-router";
  import {Toast} from "vant";
  import qs, {parse, stringify} from 'qs';
  import UserCardList from "../components/UserCardList.vue";

  const route = useRoute();
  const { tags } = route.query;

  const userList = ref([]);
  onMounted(async () => {
    // Make a request for a user with a given ID
      const userListData = await myAxios.get('/user/search/tags',{
      params: {
        tagNameList: tags
      },
      paramsSerializer: {
        serialize: params => qs.stringify(params,{ indices: false})
      }
    })
        .then(function (response) {
          console.log('/user/search/tags', response);
          return response?.data;
        })
        .catch(function (error) {
          console.log('/user/search/tags', error);
        })
      if(userListData){
        userListData.forEach(user => {
          if(user.tags){
            user.tags = JSON.parse(user.tags)
          }
        })
        userList.value = userListData;
      }
  })

  // const mockUser = {
  //   id: 15530,
  //   userName: 'Povlean',
  //   userAccount: '珀无沦',
  //   profile: '从大二开始，幡然醒悟开启自己的java逆袭之路，不满足于现状，致力于学习JAVA和编程工具的大三学生，下一年开始找工作了',
  //   gender: '男',
  //   phone: '123456',
  //   email: '1927079760@qq.com',
  //   planetCode: '15530',
  //   tags: ['Java','大三','准备实习','持续颓废'],
  //   avatarUrl: 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F202003%2F05%2F20200305125407_jaddk.thumb.400_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1676707693&t=38cfde8dc3bc4c5bc9aa6995d89098f0',
  //   createTime: new Date()
  // };

  // const userList = ref([]);

</script>

<style scoped>

</style>