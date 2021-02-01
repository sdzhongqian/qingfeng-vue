<template>
  <div>
    <a-form-model
      ref="ruleForm"
      :model="form"
      :rules="rules"
      :label-col="labelCol"
      :wrapper-col="wrapperCol"
    >
      <a-form-model-item ref="jobName" label="任务名称" prop="jobName">
        <a-input
          v-model="form.jobName"
          @blur="
            () => {
              $refs.jobName.onFieldBlur();
            }
          "
        />
      </a-form-model-item>
      <a-form-model-item ref="jobGroup" label="任务分组" prop="jobGroup">
        <a-input
          v-model="form.jobGroup"
          @blur="
            () => {
              $refs.jobGroup.onFieldBlur();
            }
          "
        />
      </a-form-model-item>

      <a-form-model-item ref="jobClassName" label="执行类" prop="jobClassName">
        <a-input
          v-model="form.jobClassName"
          @blur="
            () => {
              $refs.jobClassName.onFieldBlur();
            }
          "
        />
      </a-form-model-item>
      <a-form-model-item
        ref="cronExpression"
        label="执行时间"
        prop="cronExpression"
      >
        <a-input
          v-model="form.cronExpression"
          @input="handleinput"
          @blur="
            () => {
              $refs.cronExpression.onFieldBlur();
            }
          "
        >
          <a-icon
            slot="prefix"
            type="schedule"
            title="corn控件"
            @click="openModal"
          />
          <a-icon
            v-if="form.cronExpression"
            slot="suffix"
            type="close-circle"
            @click="handleEmpty"
            title="清空"
          />
        </a-input>
      </a-form-model-item>
      <a-form-model-item label="任务描述" prop="description">
        <a-input v-model="form.description" type="textarea" />
      </a-form-model-item>
    </a-form-model>
    <AntCron ref="innerVueCron" :data="afterCron" @ok="handleOK"></AntCron>
  </div>
</template>
<script>
import { saveOrUpdate } from "@/api/quartz/timTask";
import AntCron from "../Cron/antCron/AntCron";
import { replaceWeekName } from "../Cron/antCron/validator";
export default {
  components: {
    AntCron,
  },
  // 声明当前子组件接收父组件传递的属性
  props: {
    record: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      labelCol: { span: 4 },
      wrapperCol: { span: 14 },
      other: "",
      mybol: false,
      form: {
        id: "",
        jobName: "",
        jobGroup: "",
        jobClassName: "",
        cronExpression: "",
        description: "",
      },
      afterCron: "",
      rules: {
        jobName: [
          { required: true, message: "必填项不能为空", trigger: "blur" },
          { min: 0, max: 50, message: "长度不得大于50个字符", trigger: "blur" },
        ],
        jobGroup: [
          { required: true, message: "必填项不能为空", trigger: "blur" },
          { min: 0, max: 50, message: "长度不得大于50个字符", trigger: "blur" },
        ],
        jobClassName: [
          { required: true, message: "必填项不能为空", trigger: "blur" },
          { min: 0, max: 50, message: "长度不得大于50个字符", trigger: "blur" },
        ],
        cronExpression: [
          { required: true, message: "必填项不能为空", trigger: "blur" },
          { min: 0, max: 50, message: "长度不得大于50个字符", trigger: "blur" },
        ],
        description: [
          {
            min: 0,
            max: 500,
            message: "长度不得大于500个字符",
            trigger: "blur",
          },
        ],
      },
    };
  },
  mounted() {
    this.form = this.record;
    this.form.oldJobName = this.record.jobName;
    this.form.oldJobGroup = this.record.jobGroup;
  },
  methods: {
    resetForm() {
      this.$refs.ruleForm.resetFields();
    },
    onOk() {
      console.log(this.mybol);
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          console.log(this.form.id);
          saveOrUpdate(this.form).then((response) => {
            comsole.log(response);
          });
          this.mybol = true;
          return true;
        } else {
          this.mybol = false;
          console.log("error submit!!");
          return false;
        }
      });
      console.log("监听了 modal ok 事件" + this.mybol);
      return new Promise((resolve) => {
        resolve(this.mybol);
      });
    },
    onCancel() {
      console.log("监听了 modal cancel 事件");
      return new Promise((resolve) => {
        resolve(true);
      });
    },
    openModal() {
      this.$refs.innerVueCron.show();
    },
    handleOK(val) {
      this.form.cronExpression = val;
      console.log(this.form.cronExpression);
      this.$forceUpdate();
    },
    handleinput(evt) {
      this.form.cronExpression = evt.target.value;
    },
    handleEmpty() {
      this.handleOK("");
    },
  },
};
</script>
