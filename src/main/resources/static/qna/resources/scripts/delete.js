const deleteButton = window.document.getElementById('deleteButton');
const id = parseInt(window.location.href.split('/').at(-1).split('?')[0]);
deleteButton?.addEventListener('click', () => {
    if (!confirm('정말로 qna를 삭제할까요?')) {
        return;
    }
    cover.show('qna를 삭제하고 있습니다.\n\n잠시만 기다려 주세요.');
    const xhr = new XMLHttpRequest();
    xhr.open('DELETE', `/member/manager/read/${id}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success':
                        alert('qna를 성공적으로 삭제하였습니다.');
                        window.location.href = `../`;
                        break;
                    case 'i' :
                        alert('잘못된 접근입니다.');
                        break;
                    default :
                        alert('알 수 없는 이유로 동행을 삭제하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send();
});
