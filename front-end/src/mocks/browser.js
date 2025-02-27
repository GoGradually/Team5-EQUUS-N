import { setupWorker } from 'msw/browser';
import { handlers } from './handlers';
import { handlers2 } from './handlers2';

export const worker = setupWorker(...[...handlers, ...handlers2]);
