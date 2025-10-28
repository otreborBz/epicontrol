document.addEventListener("DOMContentLoaded", function () {
  // --- Lógica para o modal de NOVO COLABORADOR ---
  const novoColaboradorModal = document.getElementById("colaborador-modal");
  const modalForm = novoColaboradorModal.querySelector("form");
  const modalTitle = novoColaboradorModal.querySelector("h2");
  const hiddenIdInput = document.createElement("input");
  hiddenIdInput.type = "hidden";
  hiddenIdInput.name = "id";
  modalForm.appendChild(hiddenIdInput);

  const openNovoColaboradorBtn = document.getElementById("open-modal-btn");
  const closeNovoColaboradorBtn = document.getElementById("close-modal-btn");

  if (novoColaboradorModal && openNovoColaboradorBtn && closeNovoColaboradorBtn) {
    openNovoColaboradorBtn.onclick = () => {
      // Configura o modal para "Novo Colaborador"
      modalForm.reset(); // Limpa o formulário
      modalTitle.textContent = "Novo Colaborador";
      modalForm.action = "/colaboradores";
      hiddenIdInput.value = ""; // Garante que o ID oculto está vazio
      novoColaboradorModal.style.display = "flex";
    };
    closeNovoColaboradorBtn.onclick = () => {
      novoColaboradorModal.style.display = "none";
    };
  }

  // --- Lógica para o modal de EDIÇÃO ---
  const openEditModalBtns = document.querySelectorAll(".open-edit-modal-btn");

  // --- Lógica para o modal de EXCLUSÃO ---
  const deleteConfirmModal = document.getElementById("delete-confirm-modal");
  const openDeleteModalBtns = document.querySelectorAll(".open-delete-modal-btn");
  const closeDeleteModalBtn = document.getElementById("close-delete-modal-btn");
  const cancelDeleteBtn = document.getElementById("cancel-delete-btn");
  const colaboradorIdInput = document.getElementById("colaborador-id-to-delete");

  openEditModalBtns.forEach((btn) => {
    btn.addEventListener("click", function () {
      const colaboradorId = this.getAttribute("data-id");

      // Buscar dados do colaborador na API
      fetch(`/api/colaboradores/${colaboradorId}`)
        .then((response) => {
          if (!response.ok) {
            throw new Error("Falha ao buscar dados do colaborador.");
          }
          return response.json();
        })
        .then((data) => {
          // Configurar o modal para "Editar Colaborador"
          modalTitle.textContent = "Editar Colaborador";
          modalForm.action = "/colaboradores/edit";

          // Preencher os campos do formulário
          modalForm.querySelector("#nome").value = data.nome || "";
          modalForm.querySelector("#re").value = data.re || "";
          // O formato da data precisa ser YYYY-MM-DD para o input type="date"
          modalForm.querySelector("#data_admissao").value = data.data_admissao ? data.data_admissao.split("T")[0] : "";
          modalForm.querySelector("#setor").value = data.setor || "";
          modalForm.querySelector("#funcao").value = data.funcao || "";
          hiddenIdInput.value = data.id || "";

          // Abrir o modal
          novoColaboradorModal.style.display = "flex";
        })
        .catch((error) => console.error("Erro:", error));
    });
  });

  if (deleteConfirmModal) {
    // Abrir o modal de exclusão
    openDeleteModalBtns.forEach((btn) => {
      btn.addEventListener("click", function () {
        const colaboradorId = this.getAttribute("data-id");
        if (colaboradorId) {
          colaboradorIdInput.value = colaboradorId;
          deleteConfirmModal.style.display = "flex";
        }
      });
    });

    // Função para fechar o modal de exclusão
    const closeDeleteModal = () => {
      deleteConfirmModal.style.display = "none";
      colaboradorIdInput.value = "";
    };

    // Eventos para fechar o modal
    if (closeDeleteModalBtn) {
      closeDeleteModalBtn.onclick = closeDeleteModal;
    }
    if (cancelDeleteBtn) {
      cancelDeleteBtn.onclick = closeDeleteModal;
    }
  }

  // Fechar modais ao clicar fora do conteúdo
  window.onclick = function (event) {
    if (event.target == novoColaboradorModal) {
      novoColaboradorModal.style.display = "none";
    }
    if (event.target == deleteConfirmModal) {
      deleteConfirmModal.style.display = "none";
    }
  };
});