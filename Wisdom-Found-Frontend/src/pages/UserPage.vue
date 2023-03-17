<template>
  <template v-if="user">
    <van-cell title="昵称" is-link to='/user/edit' :value="user?.username"
              @click="toEdit('username',user.username,'昵称')"/>
    <van-cell title="账号" :value="user.userAccount" />
    <van-cell title="头像" is-link to='/user/edit'>
      <img style="height: 48px" :src="user.avatarUrl" />
    </van-cell>
    <van-cell title="性别" is-link to='/user/edit' :value="user.gender"
              @click="toEdit('gender',user.gender,'性别')"/>
    <van-cell title="电话" is-link to='/user/edit' :value="user.phone"
              @click="toEdit('phone',user.phone,'电话')"/>
    <van-cell title="邮箱" is-link to='/user/edit' :value="user.email"
              @click="toEdit('email',user.email,'邮箱')"/>
    <van-cell title="星球编号"  :value="user.planetCode" />
  </template>
</template>

<script setup lang="ts">
  import {useRouter} from "vue-router";
  import {onMounted, ref} from "vue";
  import {getCurrentUser} from "../services/user";

  const user = ref();

  const router = useRouter();

  const toEdit = (editKey: string,currentValue: string,editName: string) => {
    router.push({
      path: '/user/edit',
      query: {
        editKey,
        currentValue,
        editName
      }
    })
  }

  onMounted(async () => {
    user.value = await getCurrentUser();
  })
</script>

<style scoped>

</style>