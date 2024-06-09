import React, { useEffect, useState } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import { Table, message } from 'antd';
import { getUsers } from '@/services/userService';

const ListTableList = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    getUsers()
      .then(response => {
        setUsers(response.data.result);
        setLoading(false);
      })
      .catch(error => {
        message.error('Failed to load users');
        setLoading(false);
      });
  }, []);

  const columns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: 'Username',
      dataIndex: 'username',
      key: 'username',
    },
    {
      title: 'Full Name',
      dataIndex: 'fullname',
      key: 'fullname',
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: 'Date of Birth',
      dataIndex: 'dob',
      key: 'dob',
    },
    {
      title: 'Roles',
      dataIndex: 'roles',
      key: 'roles',
      render: roles => roles.join(', '),
    },
  ];

  return (
    <PageContainer>
      <Table
        dataSource={users}
        columns={columns}
        rowKey="id"
        loading={loading}
      />
    </PageContainer>
  );
};

export default ListTableList;
