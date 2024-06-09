import { ClusterOutlined, ContactsOutlined, HomeOutlined, PlusOutlined } from '@ant-design/icons';
import { GridContent } from '@ant-design/pro-components';
import { useRequest } from '@umijs/max';
import { Avatar, Card, Col, Divider, Row, Tag } from 'antd';
import React, { useState } from 'react';
import useStyles from './Center.style';
import Applications from './components/Applications';
import Articles from './components/Articles';
import Projects from './components/Projects';
import { getCurrentUser } from '@/services/userIdService';

const operationTabList = [
  {
    key: 'articles',
    tab: (
      <span>
        文章{' '}
        <span
          style={{
            fontSize: 14,
          }}
        >
          (8)
        </span>
      </span>
    ),
  },
  {
    key: 'applications',
    tab: (
      <span>
        应用{' '}
        <span
          style={{
            fontSize: 14,
          }}
        >
          (8)
        </span>
      </span>
    ),
  },
  {
    key: 'projects',
    tab: (
      <span>
        项目{' '}
        <span
          style={{
            fontSize: 14,
          }}
        >
          (8)
        </span>
      </span>
    ),
  },
];

const Center: React.FC = () => {
  const { styles } = useStyles();
  const [tabKey, setTabKey] = useState('articles');

  // Lấy thông tin người dùng từ API
  const { data: currentUser, loading } = useRequest(() => getCurrentUser());

  // Hiển thị thông tin người dùng
  const renderUserInfo = ({ fullname, email, dob, roles }) => {
    return (
      <div className={styles.detail}>
        <p>
          <ContactsOutlined style={{ marginRight: 8 }} />
          {fullname}
        </p>
        <p>
          <ClusterOutlined style={{ marginRight: 8 }} />
          {email}
        </p>
        <p>
          <HomeOutlined style={{ marginRight: 8 }} />
          {dob}
        </p>
        <p>
          <HomeOutlined style={{ marginRight: 8 }} />
          {roles?.join(', ')}
        </p>
      </div>
    );
  };

  // Hiển thị nội dung theo tab
  const renderChildrenByTabKey = (tabValue) => {
    if (tabValue === 'projects') {
      return <Projects />;
    }
    if (tabValue === 'applications') {
      return <Applications />;
    }
    if (tabValue === 'articles') {
      return <Articles />;
    }
    return null;
  };

  return (
    <GridContent>
      <Row gutter={24}>
        <Col lg={7} md={24}>
          <Card
            bordered={false}
            style={{ marginBottom: 24 }}
            loading={loading}
          >
            {!loading && currentUser && (
              <div>
                <div className={styles.avatarHolder}>
                  <Avatar size="large" src="/path/to/avatar.png" />
                  <div className={styles.name}>{currentUser.username}</div>
                </div>
                {renderUserInfo(currentUser)}
                <Divider dashed />
              </div>
            )}
          </Card>
        </Col>
        <Col lg={17} md={24}>
          <Card
            className={styles.tabsCard}
            bordered={false}
            tabList={operationTabList}
            activeTabKey={tabKey}
            onTabChange={(key) => setTabKey(key)}
          >
            {renderChildrenByTabKey(tabKey)}
          </Card>
        </Col>
      </Row>
    </GridContent>
  );
};

export default Center;
