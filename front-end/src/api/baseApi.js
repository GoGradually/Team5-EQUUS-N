import { showToast } from '../utility/handleToast';

const BASE_URL = 'https://api.feedhanjum.com';

/**
 * @typedef {Object} RequestOptions
 * @property {string} method - HTTP 메서드 (GET, POST, PUT, PATCH, DELETE)
 * @property {Object.<string, string>} headers - 요청 헤더
 * @property {string} [body] - 요청 바디 (JSON 문자열)
 */

/**
 * API 요청을 보내는 함수
 * @template T
 * @param {string} method - HTTP 메서드 (GET, POST, PUT, PATCH, DELETE)
 * @param {string} url - API URL
 * @param {Object} [params] - URL 파라미터 (옵션)
 * @param {Object} [body] - 요청 바디 (옵션)
 * @returns {Promise<T>} - JSON 파싱된 응답 데이터
 */
const request = async (method, url, params = {}, body) => {
  /** @type {RequestOptions} */
  const options = {
    method,
    credentials: 'include',
    headers: {
      'Content-Type': 'application/json',
    },
  };

  if (body) {
    options.body = JSON.stringify(body);
  }

  // URL에 파라미터 추가
  const queryString = new URLSearchParams(params).toString();
  const fullUrl =
    queryString ? `${BASE_URL}${url}?${queryString}` : `${BASE_URL}${url}`;

  try {
    const response = await fetch(fullUrl, options);
    if (!response.ok) {
      // TODO: 커스텀 에러 객체 만들어서 처리
      throw new CustomError(response.status, await response.text());
    }
    const text = await response.text();
    if (!text) {
      return null;
    } else {
      return JSON.parse(text);
    }
  } catch (error) {
    console.error('API Request', error);
    if (error.message === 'Failed to fetch') {
      showToast('서버가 응답하지 않습니다');
      throw new CustomError(500, '서버가 응답하지 않습니다');
    }
    throw error;
  }
};

class CustomError extends Error {
  constructor(status, message) {
    super(message); // 기본 Error 객체의 message 속성을 설정합니다.
    this.status = status;
    this.name = 'CustomError'; // 에러 이름을 지정할 수 있습니다.
  }
}

/**
 * API 모듈
 */
export const api = {
  /**
   * GET 요청
   * @template T
   * @param {object} props
   * @param {string} props.url - API URL
   * @param {Object} [props.params] - URL 파라미터
   * @returns {Promise<T>}
   */
  get: ({ url, params }) => request('GET', url, params),

  /**
   * POST 요청
   * @template T
   * @param {object} props
   * @param {string} props.url - API URL
   * @param {Object} [props.params] - URL 파라미터
   * @param {Object} props.body - 요청 바디
   * @returns {Promise<T>}
   */
  post: ({ url, params, body }) => request('POST', url, params, body),

  /**
   * PUT 요청
   * @template T
   * @param {object} props
   * @param {string} props.url - API URL
   * @param {Object} [props.params] - URL 파라미터
   * @param {Object} props.body - 요청 바디
   *
   * @returns {Promise<T>}
   */
  put: ({ url, body, params }) => request('PUT', url, params, body),

  /**
   * PATCH 요청
   * @template T
   * @param {object} props
   * @param {string} props.url - API URL
   * @param {Object} [props.params] - URL 파라미터
   * @param {Object} props.body - 요청 바디
   * @returns {Promise<T>}
   */
  patch: ({ url, body, params }) => request('PATCH', url, params, body),

  /**
   * DELETE 요청
   * @template T
   * @param {object} props
   * @param {string} props.url - API URL
   * @param {Object} [props.params] - URL 파라미터
   * @returns {Promise<T>}
   */
  delete: ({ url, params }) => request('DELETE', url, params),
};
