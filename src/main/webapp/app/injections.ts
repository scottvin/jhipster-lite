import { key, piqureWrapper } from 'piqure';
import { ApplicationListener } from '@/shared/alert/infrastructure/primary/ApplicationListener';
import { WindowApplicationListener } from '@/shared/alert/infrastructure/primary/WindowApplicationListener';
import { BodyCursorUpdater } from '@/module/primary/landscape/BodyCursorUpdater';
import { WindowAction } from '@/WindowAction';

const { provide, inject } = piqureWrapper(window, 'piqure');

export { provide, inject };

export const GLOBAL_WINDOW = key<WindowAction>('globalWindow');
export const APPLICATION_LISTENER = key<ApplicationListener>('applicationListener');
export const CURSOR_UPDATER = key<BodyCursorUpdater>('cursorUpdater');

export const provideWindowsTooling = (): void => {
  provide(GLOBAL_WINDOW, window);
  provide(APPLICATION_LISTENER, new WindowApplicationListener(window));
  provide(CURSOR_UPDATER, new BodyCursorUpdater(window));
};
