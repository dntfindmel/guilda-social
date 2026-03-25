export interface Usuario {
  id?: string;
  nome: string;
  email: string;
  dataNascimento?: Date;
  idade?: number;
  telefone?: string;
  cidade: string;
  estado: string;
  descricao?: string;
  interesses?: string[];
  fotoPerfil?: string;  
  latitude?: number;
  longitude?: number;
  dataCadastro?: Date;
  ativo?: boolean;
  senha?: string;
}

export interface Jogo {
  id: string;
  nome: string;
  tipo: 'TABULEIRO' | 'CARD_GAME' | 'RPG_MESA' | 'WAR_GAME';
  minJogadores: number;
  maxJogadores: number;
  tempoMedioPartida?: number;
  imagemUrl?: string;
}

export interface CriarUsuarioDTO {
  nome: string;
  email: string;
  senha: string;
  dataNascimento: Date;
  cidade: string;
  estado: string;
  descricao?: string;
  interesses?: string[];
  fotoPerfil?: string;
  latitude?: number;
  longitude?: number;
}
