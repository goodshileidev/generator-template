export const FULL_SCREEN_CLASS_NAME = 'fullscreen-enabled' as const;
/**
 * 弹窗类组件获取父级容器(适配全屏后弹窗类组件遮罩问题)
 * @returns
 */
export const getContainer = () => {
  return (document.querySelector('.' + FULL_SCREEN_CLASS_NAME) || document.body) as HTMLElement;
}
