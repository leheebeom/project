const id = parseInt(window.location.href.split('/').at(-1).split('?')[0]);

const warning = {
    getElement: () => window.document.getElementById('warning'),
    hide: () => warning.getElement().classList.remove('visible'),
    show: (text) => {
        warning.getElement().innerText = text;
        warning.getElement().classList.add('visible');
    }
};
const loadQnaArticle = () => {
    cover.show('qna 정보를 불러오고 있습니다.\n\n잠시만 기다려 주세요.');
    const xhr = new XMLHttpRequest();
    xhr.open('PATCH', window.location.href);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                form['title'].value = responseJson['title'];
                form['content'].value = responseJson['content'];
                ClassicEditor.create(form['content'], {
                    simpleUpload: {
                        uploadUrl: '../image'
                    }
                }).then(x => {
                    editor = x;
                });
            } else if (xhr.status === 403) {
                alert('k');
                window.location.href = 'https://www.google.com/search?=더+배우고+와라+애송이';
            } else if (xhr.status === 404) {
                alert('존재하지 않는 동행입니다.')
                if (window.history.length > 0) {
                    window.history.back();
                } else {
                    window.close();
                }
            } else {
                alert('서버와 통신하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
                if (window.history.length > 0) {
                    window.history.back();
                } else {
                    window.close();
                }
            }

        }
    };
    xhr.send();
};

const form = window.document.getElementById('form');

loadQnaArticle();
form['back'].addEventListener('click', () => {
    history.back();
});
form.onsubmit = e => {
    e.preventDefault();
    if (form['title'].value === '') {
        warning.show('제목을 입력해 주세요.');
        form['title'].focus();
        return false;
    }
    if (form['content'].value === '') {
        warning.show('내용을 입력해 주세요.');
        form['content'].focus();
        return false;
    }
    if(editor.getData() == '') {
        warning.show('수정할 내용을 입력해 주세요.');
        form['content'].focus();
        return false;

    }
    cover.show('qna 글을 수정하고 있어요.\n\n잠시만 기다려 주세요.');
    const xhr = new XMLHttpRequest();
    const formDate = new FormData();
    formDate.append('title', form['title'].value);
    formDate.append('content', form['content'].value);
    xhr.open('POST', window.location.href);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            cover.hide();
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseJson = JSON.parse(xhr.responseText);
                switch (responseJson['result']) {
                    case 'success' :
                        const id = responseJson['id'];
                        alert('성공');
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
let editor;

