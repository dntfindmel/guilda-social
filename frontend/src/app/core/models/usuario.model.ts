export interface Usuario {
  id?: string;
  nome: string;
  email: string;
  dataNascimento: Date;
  telefone?: string;
  cidade: string;
  estado: string;
  latitude?: number;
  longitude?: number;
  descricao?: string;
  fotoPerfil?: string;
  dataCadastro?: Date;
  ativo?: boolean;
  ultimoLogin?: Date;
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
