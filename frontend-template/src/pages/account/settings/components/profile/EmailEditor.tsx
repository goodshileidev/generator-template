import React, {useEffect, useMemo, useRef, useState} from 'react';
import {Button, Form, Input, Modal, Space, Tag, Typography, message} from 'antd';
import {CheckCircleFilled, MailTwoTone} from '@ant-design/icons';
import {useIntl} from '@umijs/max';
import useStyles from '../index.style';
import type {CurrentUser} from '../../data';
import {sendEmailVerifyCode} from '@/common/service/login';
import {updateOperatorInfo} from '@/common/api/common/login';

const {Paragraph, Text} = Typography;

type EmailEditorProps = {
  currentUser?: CurrentUser;
  onUpdated?: () => void;
};

const EmailEditor: React.FC<EmailEditorProps> = ({currentUser, onUpdated}) => {
  const {styles} = useStyles();
  const intl = useIntl();
  const [modalVisible, setModalVisible] = useState(false);
  const [form] = Form.useForm();
  const [sending, setSending] = useState(false);
  const [saving, setSaving] = useState(false);
  const [countdown, setCountdown] = useState(0);
  const timerRef = useRef<NodeJS.Timeout | null>(null);
  const watchedEmail = Form.useWatch('email', form);

  const email = currentUser?.email;
  const isVerified = currentUser?.associatedEmailVerificationStatus === 'Y' || !!email;

  useEffect(() => () => {
    if (timerRef.current) {
      clearInterval(timerRef.current);
    }
  }, []);

  useEffect(() => {
    if (modalVisible) {
      form.setFieldsValue({email, verifyCode: undefined});
    } else {
      form.resetFields();
      if (timerRef.current) {
        clearInterval(timerRef.current);
      }
      setCountdown(0);
    }
  }, [modalVisible, email, form]);

  const hasChanged = useMemo(() => {
    return watchedEmail !== undefined && watchedEmail !== email;
  }, [watchedEmail, email]);

  const handleSendCode = async () => {
    try {
      const values = await form.validateFields(['email']);
      if (!values.email) {
        return;
      }
      setSending(true);
      await sendEmailVerifyCode({email: values.email, bizType: 'account_email_verify'});
      message.success(
        intl.formatMessage({
          id: 'pages.account.email.verifySent',
          defaultMessage: '验证码已发送，请检查邮箱',
        }),
      );
      setCountdown(180);
      timerRef.current = setInterval(() => {
        setCountdown((prev) => {
          if (prev <= 1) {
            if (timerRef.current) {
              clearInterval(timerRef.current);
            }
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
    } catch (error: any) {
      if (!error?.errorFields) {
        message.error(error?.message || intl.formatMessage({
          id: 'common.request_failed',
          defaultMessage: '请求失败',
        }));
      }
    } finally {
      setSending(false);
    }
  };

  const handleSave = async () => {
    try {
      const values = await form.validateFields();
      if (!values.email || values.email === email) {
        return;
      }
      setSaving(true);
      await updateOperatorInfo({email: values.email, verifyCode: values.verifyCode});
      message.success(intl.formatMessage({id: 'common.update_success', defaultMessage: '更新成功'}));
      setModalVisible(false);
      onUpdated?.();
    } catch (error: any) {
      if (!error?.errorFields) {
        message.error(error?.message || intl.formatMessage({
          id: 'common.request_failed',
          defaultMessage: '请求失败',
        }));
      }
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className={styles.infoCard}>
      <div className={styles.cardHeader}>
        <Space align="start">
          <MailTwoTone twoToneColor="#1890ff" className={styles.mailIcon}/>
          <div>
            <Text className={styles.cardTitle}>
              {intl.formatMessage({
                id: 'system.personal_information_editing.associated_email',
                defaultMessage: '联系邮箱',
              })}
            </Text>
            <Paragraph className={styles.cardDescription}>
              {intl.formatMessage({
                id: 'system.personal_information_editing.email_description',
                defaultMessage: '用于找回密码和接收通知',
              })}
            </Paragraph>
          </div>
        </Space>
        <Space>
          <Tag color={isVerified ? 'green' : 'default'} className={styles.statusTag}>
            {isVerified
              ? intl.formatMessage({id: 'system.personal_information_editing.email_status_verified', defaultMessage: '已验证'})
              : intl.formatMessage({id: 'system.personal_information_editing.email_status_unverified', defaultMessage: '未验证'})}
          </Tag>
          <Button onClick={() => setModalVisible(true)}>
            {intl.formatMessage({id: 'system.personal_information_editing.modify_button', defaultMessage: '修改'})}
          </Button>
        </Space>
      </div>
      <div className={styles.cardStatusRow}>
        <Space>
          <CheckCircleFilled className={isVerified ? styles.verifiedIcon : styles.pendingIcon}/>
          <Text className={styles.cardValue}>{email || '--'}</Text>
        </Space>
      </div>

      <Modal
        open={modalVisible}
        title={intl.formatMessage({id: 'system.personal_information_editing.email_modal_title', defaultMessage: '修改邮箱'})}
        onCancel={() => setModalVisible(false)}
        onOk={handleSave}
        okButtonProps={{disabled: !hasChanged}}
        confirmLoading={saving}
      >
        <Paragraph type="secondary">
          {intl.formatMessage({
            id: 'system.personal_information_editing.email_modal_tips',
            defaultMessage: '我们会向新的邮箱发送验证码以确认归属。',
          })}
        </Paragraph>
        <Form layout="vertical" form={form}>
          <Form.Item
            name="email"
            label="Email"
            rules={[
              {required: true, message: intl.formatMessage({id: 'common.validator.required', defaultMessage: '请输入必填项'})},
              {type: 'email', message: intl.formatMessage({id: 'account.forget_password.invalidEmail', defaultMessage: '邮箱格式不正确'})},
            ]}
          >
            <Input placeholder="email@example.com"/>
          </Form.Item>
          <Form.Item
            name="verifyCode"
            label={intl.formatMessage({id: 'account.forget_password.enterCode', defaultMessage: '验证码'})}
            rules={[{required: true, message: intl.formatMessage({id: 'account.forget_password.enterCode', defaultMessage: '请输入验证码'})}]}
          >
            <Input
              placeholder="123456"
              addonAfter={
                <Button type="link" disabled={sending || countdown > 0 || !hasChanged} onClick={handleSendCode}>
                  {countdown > 0
                    ? `${countdown}s`
                    : intl.formatMessage({id: 'account.forget_password.getCode', defaultMessage: '获取验证码'})}
                </Button>
              }
            />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default EmailEditor;
