const form = window.document.getElementById('form');
const id = parseInt(window.location.href.split('/').at(-1).split('?')[0]);
form['back'].addEventListener('click', () => {
    window.location.href = './';
});
form.onsubmit = e => {
    e.preventDefault();
    if (form['content'].value === '') {
        warning.show('내용을 입력해 주세요.');
        form['content'].focus();
        return false;
    }
    cover.show('qna 글을 작성하고 있어요.\n\n잠시만 기다려 주세요.');
    const xhr = new XMLHttpRequest();
    const formDate = new FormData();
    formDate.append('content', form['content'].value);
    xhr.open('POST', `./${id}`);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success' :
                        alert('성공');
                        // window.location.reload();
                        break
                    case 'not_found' :
                        alert('찾을 수 없음');
                        break
                    case 'not-signed' :
                        alert('로그인 정보 유효x');
                        break
                    default:
                        warning.show('알 수 없는 이유로 qna 글을 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                }
            } else {
                warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formDate);
};