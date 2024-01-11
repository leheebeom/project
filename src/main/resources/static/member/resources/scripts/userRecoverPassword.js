const form = window.document.getElementById('form');
const warning = {
    getElement: () => window.document.getElementById('warning'),
    hide: () => warning.getElement().classList.remove('visible'),
    show: (text) => {
        warning.getElement().innerText = text;
        warning.getElement().classList.add('visible');
    }
}

form.onsubmit = e => {
    e.preventDefault();
    if (form['email'].value === '') {
        warning.show('이메일 주소를 입력해 주세요.');
        form['email'].focus();
        return false;
    }

    if (!new RegExp('^(?=.{7,50})([\\da-zA-Z_.]{4,})@([\\da-z\\-]{2,}\\.)?([\\da-z\\-]{2,})\\.([a-z]{2,10})(\\.[a-z]{2})?$').test(form['email'].value)) {
        warning.show('올바른 이메일을 입력해 주세요.');
        form['email'].focusAndSelect();
        return false;
    }

    cover.show('비밀번호 재설정 링크를 전송하고 있습니다. \n\n잠시만 기다려 주세요.');

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('email', form['email'].value);
    xhr.open('POST', './userRecoverPassword')
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success' :
                        // dejj339@naver.com ->  [dejj339, naver.com] -> [https://naver.com] 으로 됨 -> 구글 계정으로 바꿔야 될듯.
                        const esp = 'https://' + form['email'].value.split('@')[1];
                        form.classList.remove('visible');
                        window.document.getElementById('esp').setAttribute('href', esp);
                        window.document.getElementById('result').classList.add('visible');
                        break;
                    default :
                        warning.show('해당 이메일을 사용하는 회원이 없습니다.');
                        form['email'].focusAndSelect();
                }
            } else {
                warning.show('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        }
    };
    xhr.send(formData);
};