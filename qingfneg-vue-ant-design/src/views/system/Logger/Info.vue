<template>
<a-card :bordered="false">
      <a-descriptions title="用户组详情">
        <a-descriptions-item label="标题">{{form.title}}</a-descriptions-item>
        <a-descriptions-item label="内容">{{form.content}}</a-descriptions-item>
        <a-descriptions-item label="请求类型">{{form.operate_type}}</a-descriptions-item>
        <a-descriptions-item label="操作人">{{form.operate_user}}</a-descriptions-item>
        <a-descriptions-item label="操作时间">{{form.create_time}}</a-descriptions-item>
      </a-descriptions>
      <a-divider style="margin-bottom: 32px"/>
    </a-card>
</template>
<script>
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
      mybol: false,
      form: {
        id: "",
        name: "",
        short_name: "",
        order_by: 0,
        remark: "",
      },
    };
  },
  mounted() {
    this.form = this.record;
  },
  methods: {
    onCancel() {
      console.log("监听了 modal cancel 事件");
      return new Promise((resolve) => {
        resolve(true);
      });
    },
  },
};
</script>
