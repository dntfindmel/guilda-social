export interface Match {
  id: string;
  usuarioId: string;
  usuarioNome: string;
  nivelAfinidade: number;
  status: 'PENDENTE' | 'ACEITO' | 'RECUSADO';
  dataMatch: Date;
}

export interface Sugestao {
  id: string;
  nome: string;
  cidade: string;
  estado: string;
  estiloJogo?: string;
  descricao?: string;
  fotoPerfil?: string;
  nivelAfinidade: number;
}
