import { showToast } from "./handleToast";

export async function shareCode (inviteCode) {
    navigator.clipboard.writeText(`feedhanjum.com/${inviteCode}`);
    await navigator.share({
    title: '팀원들간에 건설적인 피드백을 위한 서비스, 피드한줌 - 팀스페이스에 참여해보세요!',
    text: `https://feedhanjum.com/${inviteCode}`,
    });
    showToast('초대링크를 공유해보세요');
}