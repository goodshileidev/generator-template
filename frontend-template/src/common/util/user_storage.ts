export type SafeUserInfo = {
  operatorId?: string;
  username?: string;
  displayName?: string;
  firstName?: string;
  lastName?: string;
  userRole?: string;
  email?: string;
  companyId?: string;
  language?: string;
  positionTitle?: string;
  loginWelcomeMessage?: string;
  [key: string]: any;
};

const STORAGE_KEY = 'current_login_user';
const FORBIDDEN_FIELDS = new Set([
  'password',
  'mobileVerificationCode',
  'emailVerificationCode',
  'createdBy',
  'updatedBy',
  'deletedAt',
  'dataDate',
  'requestId',
  'associatedMobileNumber',
]);

export const filterSafeUserInfo = (userInfo: any): SafeUserInfo => {
  if (!userInfo) {
    return {};
  }
  const displayName =
      userInfo.displayName ||
      `${userInfo.lastName || ''} ${userInfo.firstName || ''}`.trim() ||
      userInfo.username;
  const safeInfo: SafeUserInfo = {
    operatorId: userInfo.operatorId,
    username: userInfo.username,
    displayName,
    firstName: userInfo.firstName,
    lastName: userInfo.lastName,
    userRole: userInfo.userRole,
    email: userInfo.email,
    companyId: userInfo.companyId,
    language: userInfo.language,
    positionTitle: userInfo.positionTitle,
    loginWelcomeMessage: userInfo.loginWelcomeMessage,
  };

  Object.keys(safeInfo).forEach((key) => {
    if (FORBIDDEN_FIELDS.has(key)) {
      delete (safeInfo as any)[key];
    }
  });
  return safeInfo;
};

export const setCurrentUser = (userInfo: any) => {
  if (typeof window === 'undefined') {
    return;
  }
  const safeInfo = filterSafeUserInfo(userInfo);
  window.localStorage.setItem(STORAGE_KEY, JSON.stringify(safeInfo));
};

export const getCurrentUser = (): SafeUserInfo | null => {
  if (typeof window === 'undefined') {
    return null;
  }
  const cached = window.localStorage.getItem(STORAGE_KEY);
  if (!cached) {
    return null;
  }
  try {
    return JSON.parse(cached);
  } catch (error) {
    console.error('[user_storage] failed to parse current user cache', error);
    return null;
  }
};

export const clearCurrentUser = () => {
  if (typeof window === 'undefined') {
    return;
  }
  window.localStorage.removeItem(STORAGE_KEY);
};
