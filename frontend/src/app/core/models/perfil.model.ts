export interface Perfil {
  id: string;
  nome: string;
  email: string;
  telefone?: string;
  cidade: string;
  estado: string;
  descricao?: string;
  fotoPerfil?: string; 
  dataCadastro: Date;
  ativo: boolean;
}

export interface AtualizarPerfilRequest {
  nome?: string;
  telefone?: string;
  cidade?: string;
  estado?: string;
  descricao?: string;
  fotoPerfil?: string;
}
