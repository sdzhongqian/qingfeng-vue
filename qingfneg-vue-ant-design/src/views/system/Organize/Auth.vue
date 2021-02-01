<template>
  <div>
    <a-transfer :data-source="mockData" :list-style="{
          width: '320px',
          height: '300px',
          marginLeft: '20px'
        }" :titles="['角色', '授权角色']" :target-keys="targetKeys" :selected-keys="selectedKeys"
      :render="item => item.title" @change="handleChange" @selectChange="handleSelectChange"/>
  </div>
</template>
<script>
  import { updateAuth, getRoleAuth } from "@/api/system/organize";
  export default {
    // 声明当前子组件接收父组件传递的属性
    props: {
      record: {
        type: Object,
        default: null,
      },
    },
    data() {
      return {
        mockData: [],
        targetKeys: [],
        selectedKeys: [],
      };
    },
    mounted() {
      this.getRoleAuth();
    },
    methods: {
      getRoleAuth() {
        getRoleAuth({ id: this.record.id }).then((response) => {
          for (let i = 0; i < response.data.roleLs.length; i++) {
            response.data.roleLs[i].key = response.data.roleLs[i].id
            response.data.roleLs[i].title = response.data.roleLs[i].name
            response.data.roleLs[i].description = response.data.roleLs[i].name
            this.mockData.push({ key: response.data.roleLs[i].id, title: response.data.roleLs[i].name, description: response.data.roleLs[i].name })
          }
          for (let i = 0; i < response.data.myRoleLs.length; i++) {
            this.mockData.push({ key: response.data.myRoleLs[i].id, title: response.data.myRoleLs[i].name, description: response.data.myRoleLs[i].name })
            this.targetKeys.push(response.data.myRoleLs[i].id)
          }
        });
      },
      onOk() {
        console.log(this.targetKeys)
        console.log(this.record.id)
        updateAuth({ role_ids: this.targetKeys.join(','), id: this.record.id }).then((response) => {
          // console.log(response);
        });
        this.mybol = true;
        return new Promise((resolve) => {
          resolve(this.mybol);
        });
      },
      onCancel() {
        return new Promise((resolve) => {
          resolve(true);
        });
      },
      handleChange(nextTargetKeys, direction, moveKeys) {
        this.targetKeys = nextTargetKeys;

        // console.log('targetKeys: ', nextTargetKeys);
        // console.log('direction: ', direction);
        // console.log('moveKeys: ', moveKeys);
      },
      handleSelectChange(sourceSelectedKeys, targetSelectedKeys) {
        this.selectedKeys = [...sourceSelectedKeys, ...targetSelectedKeys];

        // console.log('sourceSelectedKeys: ', sourceSelectedKeys);
        // console.log('targetSelectedKeys: ', targetSelectedKeys);
      },
    },
  };
</script>