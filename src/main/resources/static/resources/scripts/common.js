HTMLInputElement.prototype.focusAndSelect = function () {
    this.focus();
    this.select();
}

const cover = {
    createIfNotExists: () => {
        if (cover.getElement() !== null) {
            return;
        }
        const coverImageElement = window.document.createElement('img');
        coverImageElement.classList.add('_image');
        coverImageElement.setAttribute('alt', '');
        coverImageElement.setAttribute('src', '/resources/images/_loading.png');
        const coverMessageElement = window.document.createElement('span');
        coverMessageElement.classList.add('_message');
        const coverElement = window.document.createElement('div');
        coverElement.classList.add('_object-cover');
        coverElement.append(coverImageElement, coverMessageElement);
        window.document.body.append(coverElement);
    },
    hide: () => {
        window.document.body.querySelectorAll('[data-disabled-by-cover]').forEach(input => {
            if (input.disabled) {
                input.removeAttribute('disabled');
                delete input.dataset.disabledByCover;
            }
        });
        window.document.body.classList.remove('_covering');
    },
    getElement: () => window.document.body.querySelector(':scope > ._object-cover'),
    isShown: () => window.document.body.classList.contains('_covering'),
    show: (message) => {
        window.document.body.querySelectorAll('input').forEach(input => {
            if (!input.disabled) {
                input.setAttribute('disabled', 'disabled');
                input.dataset.disabledByCover = 'yes';
            }
        });
        cover.createIfNotExists();
        const messageElement = cover.getElement().querySelector('._message');
        if (message === undefined) {
            messageElement.style.display = 'none';
        } else {
            messageElement.innerText = message;
            messageElement.style.display = 'block';
        }
        window.document.body.classList.add('_covering');
    }
};

const _writeButton = window.document.getElementById('_writeButton');
const _writeMenu = window.document.getElementById('_writeMenu');
// const searchForm = window.document.getElementById('searchForm');
_writeButton?.addEventListener('click', () => {
    if (_writeMenu?.classList.contains('visible')) {
        _writeMenu?.classList.remove('visible');
    } else {
        _writeMenu?.classList.add('visible');
    }
});
_writeMenu?.addEventListener('mouseleave', () => {
    _writeMenu?.classList.remove('visible');
});
const loginButton = window.document.getElementById('loginButton');
const myButton = window.document.getElementById('myButton');

(() => {
    const formId = 'searchForm';
    const formContainerId = 'searchFormContainer';

    const removeOrRecreateForm = () => {
        const screenWidth = window.innerWidth;
        const form = document.getElementById('searchForm');
        const formContainer = document.getElementById('searchFormContainer');

        // 창 너비가 1200px 미만인 경우 form 요소를 삭제
        if (screenWidth < 1200) {
            // form 요소가 존재하는지 확인 후 삭제
            if (form) {
                form.remove();
            }
        } else {
            // 창 너비가 1200px 이상인 경우 form 요소를 재생성하고, 원래 위치에 삽입
            if (!form) {
                const newForm = document.createElement('form');
                newForm.className = 'search-form';
                newForm.method = 'get';
                newForm.id = formId;

                const label = document.createElement('label');
                label.className = 'input-container';

                const span = document.createElement('span');
                span.hidden = true;
                span.textContent = '여행 qna을 검색해보세요!';

                const i = document.createElement('i');
                i.className = 'icon fa-solid fa-magnifying-glass';

                const input = document.createElement('input');
                input.className = 'input';
                input.maxLength = 50;
                input.name = 'keyword';
                input.placeholder = '동행 게시글을 검색해보세요!';
                input.type = 'text';

                label.appendChild(span);
                label.appendChild(i);
                label.appendChild(input);

                newForm.appendChild(label);

                // 원래 위치에 form 요소를 삽입
                formContainer.appendChild(newForm);
            }
        }
    };

    // 함수를 창 크기 조절 이벤트에 연결
    window.addEventListener('resize', removeOrRecreateForm);
    // 초기에도 함수 호출
    removeOrRecreateForm();
})();



