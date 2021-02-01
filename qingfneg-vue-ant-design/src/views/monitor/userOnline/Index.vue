<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <div class="table-page-search-wrapper">
        <a-form layout="inline">
          <a-row :gutter="24">
            <a-col :md="5" :sm="12">
              <a-form-item label="登录账号">
                <a-input v-model="queryParam.login_name" placeholder="" />
              </a-form-item>
            </a-col>
            <a-col :md="5" :sm="12">
              <a-form-item label="姓名">
                <a-input v-model="queryParam.name" placeholder="" />
              </a-form-item>
            </a-col>
            <a-col :md="5" :sm="12">
              <a-form-item label="IP地址">
                <a-input v-model="queryParam.ipaddr" placeholder="" />
              </a-form-item>
            </a-col>
            <a-col :md="5" :sm="12">
              <a-form-item label="登录地点">
                <a-input v-model="queryParam.iprealaddr" placeholder="" />
              </a-form-item>
            </a-col>
            <a-col :md="4" :sm="12">
              <a-button
                type="primary"
                @click="fetch({ results: 10, ...queryParam })"
                >查询</a-button
              >
              <a-button
                style="margin-left: 8px"
                @click="() => (queryParam = {})"
                >重置</a-button
              >
            </a-col>
          </a-row>
        </a-form>
      </div>
      <div style="margin: 0 0 10px 0">
        <a-space size="small">
          <a-button
            v-if="authButton.includes('userOnline:retreat')"
            type="danger"
            @click="retreats()"
            >强退</a-button
          >
        </a-space>
      </div>

      <a-table
        :row-selection="rowSelection"
        :columns="columns"
        :data-source="data"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        bordered
      >
        <template slot-scope="text, record, index">
          <div>
            {{ text }}
          </div>
        </template>
        <template slot="operation" slot-scope="text, record, index">
          <div class="editable-row-operations">
            <a-space :size="4">
              <div slot="actions">
                <a-space :size="4">
                  <a-button
                    size="small"
                    type="primary"
                    v-if="authButton.includes('userOnline:retreat')"
                    @click="() => retreat(record)"
                    >强退</a-button
                  >
                </a-space>
              </div>
            </a-space>
          </div>
        </template>
      </a-table>
    </a-card>
  </page-header-wrapper>
</template>
<script>
import { mapState } from "vuex";
import { getListPage, retreatData } from "@/api/monitor/userOnline";
const columns = [
  {
    title: "登录名",
    dataIndex: "login_name",
    sorter: true,
    ellipsis: true,
  },
  {
    title: "姓名",
    dataIndex: "name",
    ellipsis: true,
  },
  {
    title: "主机IP",
    dataIndex: "ipaddr",
    ellipsis: true,
  },
  {
    title: "登录地址",
    dataIndex: "iprealaddr",
    ellipsis: true,
  },
  {
    title: "浏览器",
    dataIndex: "browser",
    ellipsis: true,
  },
  {
    title: "操作系统",
    dataIndex: "os",
    ellipsis: true,
  },
  {
    title: "登录时间",
    dataIndex: "last_login_time",
    ellipsis: true,
  },
  {
    title: "操作",
    dataIndex: "operation",
    scopedSlots: { customRender: "operation" },
    width: "120px",
  },
];

export default {
  data() {
    return {
      data: [],
      pagination: {},
      loading: false,
      columns,
      selectedRows: [],
      queryParam: {
        iprealaddr: "",
        login_name: "",
        name: "",
        ipaddr: "",
      },
    };
  },
  components: {
  },
  computed: {
    ...mapState({
      // 动态主路由
      authButton: (state) => state.user.authButton,
    }),
    rowSelection() {
      return {
        onChange: (selectedRowKeys, selectedRows) => {
          this.selectedRows = selectedRows;
          console.log(
            `selectedRowKeys: ${selectedRowKeys}`,
            "selectedRows: ",
            selectedRows
          );
        },
        getCheckboxProps: (record) => ({
          props: {
            disabled: record.name === "Disabled User", // Column configuration not to be checked
            name: record.name,
          },
        }),
      };
    },
  },
  mounted() {
    this.fetch({ results: 10 });
  },
  methods: {
    handleTableChange(pagination, filters, sorter) {
      console.log(pagination);
      const pager = { ...this.pagination };
      pager.current = pagination.current;
      this.pagination = pager;
      this.fetch({
        results: pagination.pageSize,
        page: pagination.current,
        ...filters,
      });
    },
    fetch(params = {}) {
      this.loading = true;
      getListPage({...params }).then(
        (response) => {
          const pagination = { ...this.pagination };
          pagination.total = response.count;
          this.loading = false;
          this.data = response.data;
          this.pagination = pagination;
        }
      );
    },
    retreat(record) {
      this.retreatData(record.id);
    },
    retreats() {
      if (this.selectedRows.length > 0) {
        const ids = this.selectedRows.map((map) => {
          return map.id;
        });
        this.retreatData(ids);
      } else {
        this.$error({
          title: "提示：",
          content: "请选择需要操作的数据。",
          okText: "确认",
        });
      }
    },
    retreatData(ids) {
      const that = this;
      that.$confirm({
        title: "提示：",
        content: (h) => <div style="color:red;">强退会导致在线用户退出系统，真的要强退么?</div>,
        okText: "确认",
        cancelText: "取消",
        onOk() {
          retreatData(ids).then((response) => {
            if (response.success) {
              that.$success({
                title: "提示：",
                content: response.msg,
              });
              that.fetch({ results: 10 });
            } else {
              that.$error({
                title: "提示：",
                content: response.msg,
              });
            }
          });
          console.log("OK：" + ids);
        },
        onCancel() {
          console.log("Cancel");
        },
      });
    },
  },
};
</script>
<style scoped>
.list-content-item {
  margin-left: 20px;
}
</style>