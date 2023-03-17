<template>
  <van-form @submit="onSubmit">
    <van-field
        v-model="editUser.currentValue"
        :name="editUser.editKey"
        :label="editUser.editName"
        :placeholder="`请输入${editUser.editName}`"
    />
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
  </van-form>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import { ref } from "vue";
import myAxios from "../plugins/myAxios";
import {getCurrentUser} from "../services/user";

  const route = useRoute();
  const router = useRouter();

  const editUser = ref({
    editKey: route.query.editKey,
    currentValue: route.query.currentValue,
    editName: route.query.editName,
  })

  const onSubmit = async () => {
    // 异步方法必须添加 await 才可以拿到数据，否则拿到的是 promise 对象
    const currentUser = await getCurrentUser();

    console.log("---UserEditPage---" , currentUser);

    console.log("editUser.value.editKey==> ",editUser.value.editKey)
    console.log("editUser.value.currentValue==> ",editUser.value.currentValue)

    // todo [editUser.value.editKey as string]: editUser.value.currentValue,
    // [editUser.value.editKey as string] ==> null 前端缺少标识，给后端传递数据时无法封装
    const res = await myAxios.post('/user/update',{
      'id': currentUser.id,
      [editUser.value.editKey as string]: editUser.value.currentValue,
    })
    console.log("修改用户信息",res);
    if (res.code === 0 && res.data > 0) {
      console.log('修改成功');
      router.replace('/user');
    } else {
      console.log('修改失败');
    }
  };


/*const onSubmit = async () => {
  const currentUser = await getCurrentUser();

  if (!currentUser) {
    console.log('用户未登录');
    return;
  }

  console.log(currentUser, '当前用户')

  const res = await myAxios.post('/user/update', {
    'id': currentUser.id,
    [editUser.value.editKey as string]: editUser.value.currentValue,
  })
  console.log(res, '更新请求');
  if (res.code === 0 && res.data > 0) {
    console.log('修改成功')
    router.back();
  } else {
    console.log('修改失败')
  }
};*/



</script>

<style scoped>

</style>